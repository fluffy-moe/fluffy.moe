import json
import random
import datetime
from operator import itemgetter

j = []

out = {'vaccination': [], 'deinsectizaion': []}
def generate_date():
	return random.randint(1504100000, 1574200000)

def random_name(size: int):
	return ''.join(chr(ord('a') + random.randint(0, 23)) for _ in range(size))

def generate_vaccination():
	timestamp = generate_date()
	d = datetime.datetime.fromtimestamp(timestamp)
	return {
		'date':{
			'year': d.year,
			'month': d.month,
			'day': d.day
		},
		'nobivac': random_name(10),
		'injection_site': random_name(10),
		'doctor': random_name(15),
		'timestamp': timestamp,
		'status': random.randint(0, 1)
	}

def generate_deinsectizaion():
	timestamp = generate_date()
	d = datetime.datetime.fromtimestamp(timestamp)
	return {
		'date':{
			'year': d.year,
			'month': d.month,
			'day': d.day
		},
		'nobivac': random_name(10),
		'timestamp': timestamp,
		'status': random.randint(0, 1)
	}

for _ in range(100):
	out['vaccination'].append(generate_vaccination())
	out['deinsectizaion'].append(generate_deinsectizaion())

out['vaccination'] = sorted(out['vaccination'], key=itemgetter('timestamp'))
out['deinsectizaion'] = sorted(out['deinsectizaion'], key=itemgetter('timestamp'))

with open('medical.json', 'w', encoding='utf8') as fout:
	json.dump(out, fout, indent='\t', separators=(',', ': '), sort_keys=True)