# compression_with_quadtree
Compression of ppm images using quadtrees as a data structur homework

please first read the homework.pdf file.


# How to compile your code?
The code can be compiled using the following command: 
javac Main.java




# How to run your program? 
The program can be run using a command such as: java Main -i "kira.ppm" -c -t
I also added the following new tags:
1. -n <number_of_compression_levels>	: number of compression levels.
					  (If not set, the compression levels mentioned in the home work pdf will be used.)
2. -a <approximation_level>		: approximation level. Used to approximate the targeted compression levels.
					  (If not set, 0.001 will be used.)

I also provided the output of each of the following commands as '.jpg' files:
java Main -i "kira.ppm" -e		-->		noQuadtree_withEdgesDetection_0.jpg
java Main -i "kira.ppm" -c		-->		withQuadtree_noOutline_noEdges_[0-3].jpg
java Main -i "kira.ppm" -c -t 		-->		withQuadtree_withOutline_noEdges_[0-3].jpg




# Known Bugs and Limitations:

Known Bugs: If a path to a directory different from the working directory is given as output path after -o the code throws an IOException.

Limitations: Not all compression levels are reachable. For instance, in the example of "kira.ppm" 0.002, 0.004, 0.5 and 0.65 are not reachable with 0.001 approximation, and this is why in the jpg examples I provided there are only 4 images (withQuadtree_noOutline_noEdges_[0-3].jpg). However with higher approximation all those levels can be reached.
