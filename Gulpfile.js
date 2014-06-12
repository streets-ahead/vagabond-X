var gulp = require('gulp'),
    gutil = require('gulp-util'),
    less = require('gulp-less'),
    path = require('path');

gulp.task("default", ['less']);

gulp.task("less", function() {
  gulp.src(LESS_INDEX)
    .pipe(less({paths: [ path.join(__dirname, 'less') ], 
        sourceMap:true, 
        sourceMapBasepath:__dirname,
        sourceMapRootpath:'/'}))
    .pipe(gulp.dest('./resources/public/css'));
});

gulp.task('watch', ['less'], function () {
  gulp.watch('styles/**/*.less', notifyLessChange);
});
