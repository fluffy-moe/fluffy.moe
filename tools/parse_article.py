# -*- coding: utf8 -*-
import json
import csv

j = {'articles':[]}

with open('newx.csv', encoding='utf8') as fin, open('articles.json', 'w', encoding='utf8') as fout:
	csv_reader = csv.reader(fin, delimiter=',')
	for row in csv_reader:
		category = row[0]
		title = row[1]
		author = row[2]
		date = row[3]
		body = row[4]
		j['articles'].append({
			'category': category, 'title': title, 'author': author, 'date': date, 'body': body
		})
		#print(category, title, author, date, body)
	j['articles'].pop(0)
	json.dump(j, fout, indent='\t', separators=(',', ': '), ensure_ascii=False)
