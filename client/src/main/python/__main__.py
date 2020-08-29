import os
import sys
import grpc
import time
path = os.path.dirname(sys.modules[__name__].__file__)
path = os.path.join(path, 'interface')
sys.path.insert(0, path)
import client.interface.interface_pb2 as interface_pb2
import client.interface.interface_pb2_grpc as interface_pb2_grpc


def time_request_test(channel):
    stub = interface_pb2_grpc.TimeStub(channel)
    start = time.time()
    time_request = interface_pb2.TimeRequest()
    time_request.locale = interface_pb2.Locale.FRANCE
    for response in stub.GetTime(time_request):
        print(response)
    print(time.time() - start)

def image_request(channel):
    with open('/home/guillaume/Desktop/leelou', 'rb') as fd:
        data = fd.read()
    crop_image_request = interface_pb2.CropImageRequest()
    crop_image_request.image = data
    crop_image_request.n_width = 5
    crop_image_request.n_height = 7
    stub = interface_pb2_grpc.ImageCropperStub(channel)
    print(f"Sending request file: leelou, n_width: {crop_image_request.n_width}, n_height: {crop_image_request.n_height}")
    start = time.time()
    for response in stub.CropImage(crop_image_request):
        with open(f'/home/guillaume/Desktop/images/leelou_{response.x_position}_{response.y_position}.png', 'wb') as fd:
            print(f"Received leelou_{response.x_position}_{response.y_position}.png")
            fd.write(response.image)
    print(time.time() - start)

if __name__ == "__main__":
    channel = grpc.insecure_channel('127.0.0.1:9000')
    image_request(channel)


