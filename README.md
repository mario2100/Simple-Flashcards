## Simple Flashcards Plus

![logo](Logo/horizontal.png)

**Features:**
- Browse millions of flashcard sets from Quizlet and download them with a single-tap
- Create as many custom flashcard sets as you want. You can rename or delete them whenever
- Add however many custom flashcards to whichever flashcard set you want. You can update or delete any flashcard whenever
- Learn any flashcard set's material with the app's browsing or quiz modes

## How To Run This Project

This app uses the Quizlet API to download flashcard sets from the internet, which requires a Quizlet client ID. For security reasons, I don't push my Quizlet client ID to GitHub, so you're going to get an error like the one below when you try to compile the app for the first time.

<img src="https://user-images.githubusercontent.com/43277456/47573964-91938800-d90c-11e8-896e-bfd650f95a6d.PNG"/>

To fix this, you need to create a Quizlet account and get a Quizlet client ID of your own from <a href="https://quizlet.com/api/2.0/docs">here</a>. To get your API credentials, you need to click on "Developer Dashboard", which will give you a form to sign up as a Quizlet developer and show you your credentials after you're done.

After you get access to the Quizlet API, create a file named QuizletAuthConstants in the api package with a constant variable of CLIENT_ID that is set to your Quizlet client ID.

## Download

<a href="https://play.google.com/store/apps/details?id=com.randomappsinc.simpleflashcards" target="_blank">
<img src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" alt="Get it on Google Play" height="100"/></a>

## Thanks

**Huge** thanks to Zularizal for creating the app logo. You can check out his GitHub <a href="https://github.com/zularizal">here</a>.

License
=======

    Copyright 2015-present Alexander Chiou

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
