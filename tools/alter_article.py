import json

l = []
with open('articles.json', encoding='utf8') as fin, open('narticle.json', 'w', encoding='utf8') as fout:
	r = json.load(fin)
	for a in r['articles']:
		print(a["photo_filename"].split("2."))
		a['photo_filename'] = f'header{a["photo_filename"].split("2.")[0][:-1]}.png'
	json.dump(r, fout, indent='\t', separators=(',', ': '))