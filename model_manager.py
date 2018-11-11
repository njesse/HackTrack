import pickle
import numpy as np

class MLModels:
    def __init__(self):
        self.model1 = pickle.load(open("./models/weng_logit.p", "rb"))
        self.model2 = pickle.load(open("./models/gaby_knn.p", "rb"))
        self.models = {"model1": self.model1, "model2": self.model2}
        #self.models = pickle.load(open(f"./models/{model_name}.p", "rb"))

    def process(self, model_name, data):
        if model_name == 'model1':
            return self.models[model_name].predict(data)
        elif model_name == 'model2':
            print(self.models[model_name].predict(data))
            print(self.models[model_name].predict_proba(data))
            return (self.models[model_name].predict(data), self.models[model_name].predict_proba(data))
