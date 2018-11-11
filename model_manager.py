import pickle
import numpy as np

class MLModels:
    def __init__(self):
        # self.model1 = lambda x : (np.array(x) + 13).tolist()
        # self.model2 = lambda x : (np.array(x) + 30).tolist()
        self.model1 = pickle.load(open("./models/weng_logit.p", "rb"))
        self.model2 = pickle.load(open("./models/weng_knn_20.p", "rb"))
        self.models = {"model1": self.model1, "model2": self.model2}
        #self.models = pickle.load(open(f"./models/{model_name}.p", "rb"))

    def process(self, model_name, data):
        return self.models[model_name].predict(data)
