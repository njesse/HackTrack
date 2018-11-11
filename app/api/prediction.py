from flask import current_app, jsonify, request, abort, Response
from . import api

import math
import pandas as pd
import numpy as np
from time import time

# Hard coded n nearest neighbours, can be changed to customisable
n_neighbors = 25

def half_int(x):
    val=0.5 * math.ceil(2.0 * x)
    return val

#Cleaning function
def cleaning(df_acc):
    # Rename df_acc column, 'train' -> 'mode'
    df_acc.columns = ['TI', 'TS', 'x', 'y', 'z', 'mode', 'dataset']

    # Drop TS column and replace transportation mode into integer
    df_acc = df_acc.replace({'mode': {'Train': 1, 'Walking': 0}})

    # Convert TI value to integer
    df_acc['TI'] = df_acc['TI'].apply(lambda x: half_int(x))

    # Group by and get mean by TI as key
    ndf_acc = df_acc.groupby(['TI', 'dataset'], as_index=False)[['x', 'y', 'z', 'mode']].mean()

    # Clean filtered data
    ndf_acc.columns = ['TI', 'dataset', 'acc_x', 'acc_y', 'acc_z', 'mode']
    
    return ndf_acc


@api.route("/predict/<model_name>", methods=['POST'])
def predict(model_name):
    # Expects user data i.e gyro, accelero etc (array of array)
    # user_data = request.args.get('user_data')
    user_data = request.json

    try:
        # df_acc = pd.read_csv("./acc_3.csv")
        df_acc = pd.read_csv(user_data)
        df = cleaning(df_acc)
        
        # Magnitude dataframe
        df['acc_magnitude']=(df['acc_x']**2+df['acc_y']**2+df['acc_z']**2)**(1/2.0)
        
        n, o = 10, 15
        X_new=[df['acc_magnitude'].values[x:x+n] for x in range(0,len(df['acc_magnitude'])-n+1, o) if (df.iloc[x]['mode'] == df.iloc[x+n-1]['mode'])]
        X_new=np.asarray(X_new)

        Y_new=[df['mode'].values[x] for x in range(0,len(df['mode'])-n+1, o) if df.iloc[x]['mode'] == df.iloc[x+n-1]['mode']]
        Y_new=np.asarray(Y_new)

        times=[df['TI'].values[x] for x in range(0,len(df['TI'])-n+1, o) if df.iloc[x]['mode'] == df.iloc[x+n-1]['mode']]

        times_ref = df_acc
        times_ref['TI'] = df['TI'].apply(lambda x: half_int(x))
        times_ref = times_ref.loc[times_ref['TI'].isin(times)]
        times_ref = times_ref.drop_duplicates(subset='TI', keep="last")['TS']

        # Modifying data
        predVals = current_app.mlmodels.process(model_name, X_new)

        activity = {0: "walk", 1: "train"}

        act, acc = predVals

        output = [{f"val{x}":
            {"activity": activity[act[x]], "accuracy": acc[x][int(act[x])], "timestamp": times_ref.iloc[x]}
            } for x in range(len(act))]

        # current_app.logger.warn(f"Modifed array is {predVals}")
        # current_app.logger.warn("====================================+")

    except Exception as e:
        current_app.logger.error(f"Caught an error : {e}", exc_info=True)
        abort(Response("Internal system error, please try again", 500))

    return jsonify(output=output)


    '''
    try:
        current_app.logger.warn("====================================+")
        current_app.logger.warn(f"Received following response : {user_data}")

        temp_data = np.zeros((len(user_data), 8))
        for i, data in enumerate(user_data):
            temp_data[i, :3] = data[:3]
            temp_data[i, 3] = np.linalg.norm(data[:3])
            temp_data[i, 4:7] = data[3:]
            temp_data[i, 7] = np.linalg.norm(data[3:])

        current_app.logger.warn(temp_data[:, 3], temp_data[:, 7])

        # Modifying data
        predVals = current_app.mlmodels.process(model_name, temp_data)

        activity = {0: "walk", 1: "train"}

        output = [{f"val{x}":
            {"activity": activity[predVal], "accuracy": 82, "timestamp": "2018-11-11"}
            } for x, predVal in enumerate(predVals)]

    
        predVal = [{"readingVal1":
                       {"Act": "train", "accuracy": 85.2, "timestamp": "2018-12-01"}
                   },
                   {"readingVal2":
                       {"Act": "walk", "accuracy": 65.34, "timestamp": "2011-08-02"}
                   }, 
                   {"readingVal3":
                       {"Act": "bus", "accuracy": 32.2, "timestamp": "2042-02-11"}
                   }]  
        
        current_app.logger.warn(f"Modifed array is {predVals}")
        current_app.logger.warn("====================================+")
        '''
