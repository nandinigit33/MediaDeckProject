import os
import numpy as np
import pandas as pd

from sklearn.metrics.pairwise import cosine_similarity
from sklearn.neighbors import NearestNeighbors

import csv
import mysql.connector
from sklearn.metrics.pairwise import linear_kernel
import time
import re
from scipy.sparse import csr_matrix
import sklearn as sk
import scipy as sc

"""
big thanks to the following blog post for the information 
https://www.analyticsvidhya.com/blog/2020/11/create-your-own-movie-movie-recommendation-system/


using item to item collaborative filtering with the nearest neighbor algorithm
I've found this to give better results compared to user to item filtering

working on the movielens data set from kaggle, specifically training on a subsection of the rating.csv file
since speed will be of importance for this project

make sure the following command is run to install python dependencies:
pip3 install scikit-learn np pandas scipy mysql-connector-python
or pip
"""
print("im in python")
#start_time = time.time()
#conecting to db
mydb = mysql.connector.connect(
  host="localhost",
  user="moviedock",
  password="Int3r$te11@r",
  database="moviedock"
)
print("im still in python")

#user_ratings = pd.read_csv(r"C:\Users\Dyerm\code\archive\rating.csv")
user_ratings = pd.read_csv(r"small_rating.csv")
movie_info = pd.read_csv(r"movie.csv")
movie_ids = pd.read_csv(r"link.csv")



#print("--- %s seconds ---" % (time.time() - start_time))
mycursor = mydb.cursor()

#get apiid and review score from database
mycursor.execute("SELECT apiid, star_rating, user_id FROM review JOIN movie ON (review.movie_id=movie.id)")

myresult = mycursor.fetchall()


#convert database info to dataframe
list_of_ids = []
list_of_scores = []
user_ids = []
for x in myresult:
  list_of_ids.append(x[0])
  list_of_scores.append(x[1])
  user_ids.append(x[2])

list_of_stripped_ids = []
for x in list_of_ids:
  x = x.strip("tt")
  x = x.strip("00")
  list_of_stripped_ids.append(x)
   
#create a list of ids for df (in this case will make 0)




data = {'userId': user_ids, 'imdbId' : list_of_stripped_ids, 'rating' : list_of_scores}

moviedock_user_scores = pd.DataFrame(data)

moviedock_user_scores['imdbId']=moviedock_user_scores['imdbId'].astype(int)
moviedock_user_scores['rating']=moviedock_user_scores['rating'].astype(np.float16)

print(moviedock_user_scores)
#getting the movieId from movie_ids so we can add to total ratings
#moviedock_user_scores = moviedock_user_scores.merge(movie_ids, on='imdbId')

print(moviedock_user_scores)
mycursor.execute("SELECT apiid, favorite_user_id FROM user_favourite_movies JOIN movie ON (user_favourite_movies.favorite_movie_id=movie.id)")
myresult = mycursor.fetchall()



#convert database info to dataframe
list_of_ids1 = []
user_ids1 = []
for x in myresult:
  list_of_ids1.append(x[0])
  user_ids1.append(x[1])


list_of_stripped_ids1 = []
for x in list_of_ids1:
  x = x.strip("tt")
  x = x.strip("00")
  list_of_stripped_ids1.append(x)


list_of_scores1 = [5] * len(list_of_ids1)

data1 = {'userId': user_ids1, 'imdbId' : list_of_stripped_ids1, 'rating' : list_of_scores1}
moviedock_favorites = pd.DataFrame(data1)

moviedock_favorites['imdbId']=moviedock_favorites['imdbId'].astype(int)
moviedock_favorites['rating']=moviedock_favorites['rating'].astype(np.float16)

moviedock_favorites = pd.concat([moviedock_favorites, moviedock_user_scores])
#print("here it is ")
#print(moviedock_favorites)
moviedock_favorites.reset_index()
moviedock_favorites = moviedock_favorites.merge(movie_ids, on='imdbId')
#print(moviedock_favorites)
moviedock_favorites['imdbId']=moviedock_favorites['imdbId'].astype(int)
moviedock_favorites['rating']=moviedock_favorites['rating'].astype(np.float16)

#not using as results aren't as accurate
#this was tested with user to item correlation
#the problem being it pairs with a specific user and gives recs for their favorite movie
#even with some testing this could throw in some odd ball movies
#also tested with spearmans/pearson correlation
def return_similar_movies_cosine(pt, id):
  pt = pd.DataFrame(sk.metrics.pairwise.cosine_similarity(pt))
  
  
  movie_similarity = pt.loc[id].sort_values(ascending=False).head(10)

  # removes the movie testing against others by removing self (will always have 1 as similarity)
  #movie_similarity = movie_similarity.tail(-1)
  movie_similarity = movie_similarity.reset_index()
  movie_similarity.columns = ['movieId', 'similarity']
  

  return movie_similarity



#creating a matrix of users and movies ranking
movie_user_matrix = pd.pivot(user_ratings, values ='rating',columns ='userId', index ='movieId')

# working on this to create a compressed pivot table right away to save time
# c_matrix = sc.sparse.coo_matrix(user_ratings,values ='rating',columns ='userId', index ='movieId')

#zero out NAN
movie_user_matrix = movie_user_matrix.fillna(0)

#movie_user_matrix.to_csv("movie_user_matrix.csv")


#cleaning data
#no_user_voted = user_ratings.groupby('movieId')['rating'].agg('count')
#no_movies_voted = user_ratings.groupby('userId')['rating'].agg('count')


#scrubbing out data causes out of bounds errors
#movie_user_matrix = movie_user_matrix.loc[no_user_voted[no_user_voted > 10].index,:]
#movie_user_matrix = movie_user_matrix.loc[:,no_movies_voted[no_movies_voted > 50].index]

#compressing the matrix and getting rid of 0s
c_matrix = csr_matrix(movie_user_matrix)



knn = NearestNeighbors(metric='cosine', algorithm='brute', n_neighbors=20, n_jobs=-1)
knn.fit(c_matrix)



movie_user_matrix.reset_index(inplace=True)


#just need to modify to take movieId as input and we're gucci
def get_movie_recommendation(movie_id, userId):
    n_movies_to_reccomend = 6
    movie_list = movie_info[movie_info['movieId']==movie_id]
    #print(movie_list)
    if len(movie_list):
      #movie_idx = movie_list.iloc[0, :]['movieId']
      #movie_idx = movie_list.iloc[0]['movieId']
      #movie_idx = movie_list[movie_list['movieId']== movie_id].index[1]
      movie_idx = movie_id

      
      try:
        movie_idx = movie_user_matrix[movie_user_matrix['movieId'] == movie_idx].index[0]
      except IndexError:
        return
      
      #this line only works when matrix is crs matrix, not sure why?
    
      distances , indices = knn.kneighbors(c_matrix[movie_idx],n_neighbors=n_movies_to_reccomend) 
      rec_movie_indices = sorted(list(zip(indices.squeeze().tolist(),distances.squeeze().tolist())),key=lambda x: x[1])[:0:-1]
      recommend_frame = []
      for val in rec_movie_indices:
            movie_idx = movie_user_matrix.iloc[val[0]]['movieId']
            idx = movie_info[movie_info['movieId'] == movie_idx].index
            recommend_frame.append({'Title':movie_info.iloc[idx]['title'].values[0],'Distance':val[1],'movieId':int(movie_idx),'id':userId})
      df = pd.DataFrame(recommend_frame,index=range(1,n_movies_to_reccomend))
      
      return df
    else:
      return -1
    


    #this is displaying a list of all of the user rankings of a movie
    
    #movie_x = movie_user_matrix[movie_user_matrix['movieId']== movie_id].index[0]
    #distances, indices = knn.kneighbors(user_rating_of_movie, True)

    
    #rec_movie_indices = sorted(list(zip(indices.squeeze().tolist(),distances.squeeze().tolist())),key=lambda x: x[1])[:0:-1]
  




#1 is toystory
#print(get_movie_recommendation(27664))




#print("-----------------")
#print(moviedock_input)

#remove bad reviews
#moviedock_input = moviedock_input.drop(moviedock_input[moviedock_input.rating<3.5].index)
#print(moviedock_input)

moviedock_favorites = moviedock_favorites.drop(moviedock_favorites[moviedock_favorites.rating<3.5].index)
#print(moviedock_favorites)
moviedock_output = pd.DataFrame()



for index, row in moviedock_favorites.iterrows():
  #print(row['movieId'],row['userId'])
  df = get_movie_recommendation(row['movieId'],row['userId'])
  
  moviedock_output = pd.concat([df,moviedock_output])

moviedock_output = moviedock_output.merge(movie_ids, on='movieId')

moviedock_output = moviedock_output.drop(columns=['Distance', 'movieId', 'tmdbId'])
#print(moviedock_output)

#shuffling rows and cleaning up data for csv
moviedock_output = moviedock_output.sample(frac = 1)
moviedock_output['id'] = moviedock_output['id'].astype(int)
#moviedock_output=moviedock_output.reindex(columns=['id','Title','imdbId'])
moviedock_output=moviedock_output.reindex(columns=['id','imdbId'])

print("im in it!")
moviedock_output = moviedock_output.drop_duplicates()
moviedock_output.to_csv('movie_rec_output.csv', index=False, header=False )


