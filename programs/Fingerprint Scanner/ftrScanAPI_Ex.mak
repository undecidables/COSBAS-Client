TARGET=ftrScanAPI_Ex

CFLAGS=-Wall -O2 -I./       

FTRSCANAPI_DLIB=libScanAPI.so -lm -lusb

all: $(TARGET)

$(TARGET): $(TARGET).c
	$(CC) $(CFLAGS) -o $(TARGET)-0 $(TARGET).c $(FTRSCANAPI_DLIB)

clean:
	rm -f $(TARGET)-0
