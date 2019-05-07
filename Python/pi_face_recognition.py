from imutils.video import VideoStream
from imutils.video import FPS
import face_recognition
from firebase import firebase
from google.cloud import storage
import argparse
import imutils
import pickle
import time
from datetime import date
import cv2
import logging

storage_client = storage.Client.from_service_account_json("Notify-66eb14c0c7fc.json")
bucket  = storage_client.get_bucket("reports2")

ap = argparse.ArgumentParser()
ap.add_argument("-c", "--cascade", required=True,
                help="Path to haarcascade file")
ap.add_argument("-e", "--encodings", required=True,
                help="Path to serialized db of facial encodings")
args=vars(ap.parse_args())

print("Getting encoding + haar cascade")
data = pickle.loads(open(args["encodings"], "rb").read())
detector = cv2.CascadeClassifier(args["cascade"])

print("Streaming...")
#vs = VideoStream(src=0).start()
vs = VideoStream(usePiCamera=True).start()

firebase = firebase.FirebaseApplication('https://notify-64bd0.firebaseio.com', None)

time.sleep(2.0)

fps = FPS().start()

while True:
    frame = vs.read()
    frame = imutils.resize(frame, width=500)

    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)

    rects = detector.detectMultiScale(gray, scaleFactor=1.1,
                                      minNeighbors=5, minSize=(30,30))

    boxes=[(y,x+w,y+h,x) for (x,y,w,h) in rects]

    encodings = face_recognition.face_encodings(rgb, boxes)
    names = []

    for encoding in encodings:
        matches = face_recognition.compare_faces(data["encodings"],
                                                 encoding)
        name="Unknown"

        if True in matches:
            matchedIdxs = [i for(i, b) in enumerate(matches) if b]
            counts = {}

            for i in matchedIdxs:
                name = data["names"][i]
                counts[name] = counts.get(name, 0) + 1

            name = max(counts, key=counts.get)
        names.append(name)

        for((top, right, bottom, left), name) in zip(boxes, names):
            cv2.rectangle(frame, (left, top), (right, bottom),
                          (255, 0, 0), 2)
            y = top - 15 if top - 15 > 15 else top + 15
            cv2.putText(frame, name, (left, y), cv2.FONT_HERSHEY_SIMPLEX,
                        0.75, (0, 255, 0),2)
            img_name = str(time.asctime(time.localtime(time.time()))).replace(" ","")
            img_name = img_name.replace(":","")
            try:
                cv2.imwrite(img_name+".jpg", frame)
                blob = bucket.blob("notify-me/"+img_name)
                blob.upload_from_filename(img_name+".jpg")
                blob.make_public()
                result = firebase.post('/reports',{'name':name,'time':time.asctime(time.localtime(time.time())), 'date':date.today(),'url':blob.public_url})
            except Exception as ex:
                print("Problems with postings")
                logging.exception("Problems with postings")
                cv2.imwrite("failed_uploads/"+img_name+".jpg", frame)


    cv2.imshow("Private Eye", frame)
    key = cv2.waitKey(1) & 0xFF

    if key == ord("q"):
        break

    fps.update()
    fps.stop()
    print("Time elapsed: {:.2f}".format(fps.elapsed()))
    print("Approx FPS: {:.2f}".format(fps.fps()))

cv2.destroyAllWindows()
vs.stop()
