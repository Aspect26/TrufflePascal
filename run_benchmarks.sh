truppleGraalBenchmark(){
    name="$1"
    cd "./benchmark-codes/Pascal/$name"
    mkdir tmp
    mkdir output_trupple_graal
    for source in `find . -maxdepth 1 -type f`
    do
        source_name=`echo $source | cut -d\. -f2`
        cp "$source" "./tmp"
        echo "NOW: " "$source"
        ../../graalvm/bin/java -polyglot -jar "../trupple.jar" "-std=turbo" "./tmp/$source" > "./output_trupple_graal/${source_name}.xml"
    done
    rm -R "./tmp"
    cd "../../../"
}

truppleBenchmark(){
    name="$1"
    cd "./benchmark-codes/Pascal/$name"
    mkdir tmp
    mkdir output_trupple
    for source in `find . -maxdepth 1 -type f`
    do
        source_name=`echo $source | cut -d\. -f2`
        cp "$source" "./tmp"
        java -jar "../trupple.jar" "-std=turbo" "./tmp/$source" > "./output_trupple/${source_name}.xml"
    done
    rm -R "./tmp"
    cd "../../../"
}

fpcBenchmark(){
    name="$1"
    cd "./benchmark-codes/Pascal/$name"
    mkdir tmp
    mkdir output_fpc
    for source in `find . -maxdepth 1 -type f`
    do
        source_name=`echo $source | cut -d\. -f2`
        cp "$source" "./tmp"
        fpc "./tmp/$source"
        "./tmp$source_name" > "./output_fpc/${source_name}.xml"
    done
    rm -R "./tmp"
    cd "../../../"
}

javaBenchmark(){
    name="$1"
    cd "./benchmark-codes/Java/$name"
    mkdir tmp
    mkdir output 2>/dev/null
    for source in `find . -maxdepth 1 -type f -regex ".*\.java"`
    do
        source_name=`echo $source | cut -d\. -f2 | cut -d\/ -f2`
        cp "$source" "./tmp"
        javac "./tmp/$source"
        java -cp "./tmp" "$source_name" > "./output/${source_name}.xml"
    done
    rm -R "./tmp"
    cd "../../../" 
}

slBenchmark() {
    name="$1"
    cd "./benchmark-codes/SimpleLanguage/$name"
    mkdir tmp
    mkdir output
    for source in `find . -maxdepth 1 -type f`
    do
        source_name=`echo $source | cut -d\. -f2`
        cp "$source" "./tmp"
        echo "NOW: " "$source"
        ../../graalvm/bin/java -polyglot -cp ../simplelanguage/target/classes com.oracle.truffle.sl.SLMain "./tmp/$source" > "./output/${source_name}.xml"
    done
    rm -R "./tmp"
    cd "../../../"
}

benchmarks="bubblesort matrix_multiplication fibonacci"
benchmarks="count"
for benchmark in $benchmarks
do
    truppleGraalBenchmark "$benchmark"
    # truppleBenchmark "$benchmark"
    # fpcBenchmark "$benchmark"
    # javaBenchmark "$benchmark"
done

benchmarks="fibonacci"
benchmarks="count"
for benchmark in $benchmarks
do
    slBenchmark "$benchmark"
done
