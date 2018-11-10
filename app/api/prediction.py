from flask import current_app, jsonify, request, abort, Response
from . import api

from time import time

# Hard coded n nearest neighbours, can be changed to customisable
n_neighbors = 25

@api.route("/predict/<model_name>", methods=['POST'])
def predict(model_name):
    # Expects user data i.e gyro, accelero etc (array of array)
    # user_data = request.args.get('user_data')
    user_data = request.json

    try:
        current_app.logger.warn("====================================+")
        current_app.logger.warn(f"Received following response : {user_data}")

        # Modifying data
        predVal = current_app.mlmodels.process(model_name, user_data)

        current_app.logger.warn(f"Modifed array is {predVal}")
        current_app.logger.warn("====================================+")

    except Exception as e:
        current_app.logger.error(f"Caught an error : {e}", exc_info=True)
        abort(Response("Internal system error, please try again", 500))

    return jsonify(output=predVal)

'''
@api.route("/predict/<service_name>/<client_name>", methods=['POST'])
def predict(service_name, client_name):
    start = time()
    # Receive image path as reference for similarity search
    input_imgs = request.data

    # input_imgs = [b64decode(input_imgs) if verify_b64(input_imgs) else input_imgs]
    instances = [{"b64": input_imgs.decode('utf-8') if verify_b64(input_imgs) else b64encode(input_imgs).decode('utf-8')}]

    current_app.logger.warn(f"Time taken at pre-predict is {time() - start}.")

    try:
        # Compute nearest neighbors distance and index
        neighbor_path, neighbor_sim = current_app.fvec_manager.direct_search_nn(instances, n_neighbors, client_name)

        start1 = time()
        neighbor_prop = bundle_json(neighbor_path, neighbor_sim)
        current_app.logger.warn(f"Time taken at post-predict is {time() - start1}.")

        return jsonify(neighbor_prop=neighbor_prop)
    except Exception as e:
        current_app.logger.error(f"Caught an error : {e}", exc_info=True)
        abort(Response("Internal system error, please try again", 500))
'''

'''
# Convert lists of neighbor paths and similarity score to JSON with individual object
def bundle_json(neighbor_path, neighbor_sim):
    # Remove root image directory from path
    # neighbor_path = [ [path[len_root_dir:] for path in n_path] for n_path in neighbor_path ]

    # Convert to list so we can send it through JSON (JSON does not understand numpy array)
    neighbor_sim = neighbor_sim.tolist()

    # Wrap around to be sent as json
    neighbor_prop = [{'imgpath' : path, 'similarity' : sim} \
                     for n_path, n_sim in zip(neighbor_path, neighbor_sim) \
                     for path, sim in zip(n_path, n_sim)]

    return neighbor_prop
'''