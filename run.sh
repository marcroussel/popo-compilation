rm -rf test.txt
java -cp target/classes popo.compilation.Main > test.txt

cd msm
#gcc msm.c -o msm.exe
./msm.exe ../test.txt
cd ..