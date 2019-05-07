from imutils import paths
import face_recognition
import argparse
import pickle
import cv2
import os

ap = argparse.ArgumentParser()
ap.add_argument("-i", "--dataset", required=True,
                help="path to directory of faces")
ap.add_argument("-e", "--encodings", required=True,
                help="path to serialized db of faces")
ap.add_argument("-d", "--detection-method", type=str, default="cnn",
                help="face detection method either hog or cnn")
args = vars(ap.parse_args())

print("Grabbing faces from dataset paths")
imgPaths = list(paths.list_images(args["dataset"]))

knownEncodings = []
knownNames = []

for(i, imgPath) in enumerate(imgPaths):

    print("Processing images {}/{}".format(i+1,
                                           len(imgPaths)))
    name = imgPath.split(os.path.sep)[-2]

    img = cv2.imread(imgPath)
    rgb = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)

    boxes = face_recognition.face_locations(rgb,
                                            model=args["detection_method"])

    encodings = face_recognition.face_encodings(rgb, boxes)

    for encoding in encodings:
        knownEncodings.append(encoding)
        knownNames.append(name)

print("Serializing encodings...")
data = {"encodings":knownEncodings, "names": knownNames}
f = open(args["encodings"], "wb")
f.write(pickle.dumps(data))
f.close()
