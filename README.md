# OpenCV_Projects

This is for all my little projects relating to OpenCV, including things such as sending images over sockets, color filtering, and classifier training.

The biggest project in here (which I probably should create a repository for itself) is VisionUtility, which is a JFrame GUI that takes from either a file or camera source, and processes it with parameters gained from the user and displays a overlayed visual of the processing in real time. I am also working on a classifier trainer to remove the command line from OpenCV's cascade classifier trainer to make it easier on the user, as well as built in instructions. Eventually it will also allow real-time previews of the classifier.

Along with VisionUtility was a program designed to offload vision processing from a real-time java application to a raspberry pi. This was designed to dynamically allocate new threads to process images taken from webcams and security cameras. It's original use case was for robotics.

For the complete documentation of the VisionUtility, see here: [Planning](https://drive.google.com/file/d/1oUWZMySb0Yx_ie1-DM012YmRw7UKMXhd/view?usp=sharing) | [Implementing](https://drive.google.com/file/d/1M-oMSEsxbxnVHhJzK5iFEUa3H-OvX_lv/view?usp=sharing)
<br />For a video of how it would be used in junciton with a Raspberry Pi, see here: [Video](https://drive.google.com/file/d/185BRYFjqF4xfZsQCmbAQN_-GzSD0xTQ0/view?usp=sharing)

For these to work, download opencv 2.4.13
http://opencv.org/downloads.html
