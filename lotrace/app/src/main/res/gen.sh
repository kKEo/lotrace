
for size in mdpi hdpi xhdpi xxhdpi xxxhdpi; do
    mkdir -p mipmap-$size
    convert -size 48x48 xc:white -gravity center -pointsize 20 -draw "text 0,0 'M'" mipmap-$size/ic_launcher.png
    convert -size 48x48 xc:white -gravity center -pointsize 20 -draw "text 0,0 'M'" mipmap-$size/ic_launcher_round.png
done
