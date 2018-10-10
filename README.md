# Coding-challenge-presto

Utilizing the Android framework, design a mobile app that will show a series of images as a
gallery, fetched from Flickr’s API.
Specifics
The image data you display should be fetched from the following endpoint:
https://api.flickr.com/services/rest/?api_key=949e98778755d1982f537d56236bb
b42&amp;method=flickr.photos.search
Each entry in the list structure should display:

a. Image
b. Image size
c. Image dimensions
d. Title

More information regarding Flickr’s API can be accessed at https://www.flickr.com/services/api/.
Requirements
This challenge is centered around gauging your knowledge of the Android application flow.
The following should be implemented in order to complete this challenge:
1. A network layer that fetches image and applicable meta data from Flickr
2. A means of displaying this information in a list structure
Scalability is paramount. The code you build should not end up being a bottleneck down the
road (will this have to be rebuilt when it’s pulling from millions of images from many sources?).