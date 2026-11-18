rm -rf test.txt
java -cp target/classes popo.compilation.Main ./src/main/resources/samples/test_loops_conds.c > test.txt

cd msm
gcc msm.c -o msm.exe
./msm.exe ../test.txt
cd ..

# mvn clean test