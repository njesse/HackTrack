import base64
import pprint 
import requests
import json
import numpy as np 

pp = pprint.PrettyPrinter(indent=4)

content_type= "application/json"
headers = {'content-type': content_type}

# input_data = [13.4, 32.54, 43.2323, 43.434, 54.65]
# input_data = [[13.4, 32.54, 43.2323, 43.434, 54.65], [44, 65.54, 324.43, 54.3, 2.322]]
# input_data = [[-1.27947235e-01, -6.27358246e-01, 7.76403809e-01, 9.87527008e+01, -3.59243529e+02, -2.88740537e+02, 1.00635567e+00, 4.71358787e+02]]
input_data = [[-1.27947235e-01, -6.27358246e-01, 7.76403809e-01, -3.59243529e+02, -2.88740537e+02, 1.00635567e+00]]
data = json.dumps(input_data)

#data = json.dumps(data)

json_response = requests.post("http://localhost:3030/api/v1/predict/model2", headers=headers, data=data)

print(f"Input sent : {input_data}")
print("----------JSON_response-----------")
print(json_response.status_code)
print(json_response.status_code == 200)
print("----------Nearest Neighbor Path + Distance----------")
response = json.loads(json_response.text)
pp.pprint(response)
