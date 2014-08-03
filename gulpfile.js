var gulp  = require('gulp')
var shell = require('gulp-shell')

gulp.task('deploy', shell.task([
    'gradle deploy'
]))