# ResultProgressBar
_This library provides easy to use and customizable Progress Bar for Android_
![resultprogressbar](https://user-images.githubusercontent.com/20640251/62414540-9157b400-b625-11e9-99cb-9d6081d3efb2.gif)

## Setup
   Add this line to your dependency{} block in app build.gradle file<br>
   <b>implementation 'com.sadwyn.resultprogressbar:resultprogressbar:1.0.0'</b><br>
   
   Also you need to add mavenLocal() repository to your top gradle file.
   <img width="628" alt="Screenshot 2019-08-04 at 0 45 12" src="https://user-images.githubusercontent.com/20640251/62417139-4489d280-b651-11e9-8249-bc2f844ce051.png">

### Attributes

| Name  | Description |
| ------------- | ------------- |
| app:progressDrawable | R.drawable.<resource> you want to be used for progress
| app:successDrawable | R.drawable.<resource> you want to be used for success animation
| app:failureDrawable | R.drawable.<resource> you want to be used for failure animation
| app:borderShape | R.drawable.<resource> you want to be used for progress bar background 
| app:progressDrawableMargin | Int - inner margin of the progress drawable
| app:successDrawableMargin | Int - inner margin of the sucesss drawable
| app:failureDrawableMargin | Int - inner margin of the failure drawable
| app:progressInterpolator | type of progress behaviour (linear, accelerate, decelerate, accelerate_decelerate)
| app:progressSpeed | speed of the progressbar(slow, normal, fast)
| app:changeStateType | the type of animation for change progress state (flip, alpha, scale)


           
 
