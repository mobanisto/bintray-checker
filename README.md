# Change what needs to be changed

    find . -type f | xargs sed -i -e "s/web-template-basic/something-else/g"
    find . -type f -name "*.xml" -or -name "*.sh" | xargs sed -i -e "s/changeme/something-else/g"
