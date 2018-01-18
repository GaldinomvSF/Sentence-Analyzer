# Sentence-Sentiment-Analyzer

This Java Application evaluates the Sentiment of a Sentence based on a data set with previous reviews.

# Installion

This repository contains a Eclipse project built with Maven. In order to use this project the following steps will be necessary:

1) Fork this repository and download it locally.
2) On Eclipse use the File-> Import -> Existing project into Workspace -> Select the root of this repository.

# Usage

The file App.java analyzes the sentiment of a string inputed by user. Word-by-Word is analyzed against a series of previous reponses stored in the file "src/main/resource/file/reviews.txt" which scales the sentiment from -2 to 2. Considering -2 very bad/sab and 2 very good/happy. The reviews file can be updated or changed in order to give more accurate responses.

# Credits

This project was a homework assignment I submit as part of the Data Structure & Software Design class from EDX - University of Pensylvania. This assignment was created by Professor: Chris Murphy.

