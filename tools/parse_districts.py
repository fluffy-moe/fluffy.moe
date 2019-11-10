# -*- coding: utf-8 -*-
import json
j = {'cities': [], 'districts': {}}
with open('output.json', encoding='utf8') as fin, open('district.json', 'w', encoding='utf8') as fout:
	r = json.load(fin)
	for x in r:
		j['cities'].append(x['CityEngName'])
		j['districts'].update({x['CityEngName']: [area['AreaEngName'].split()[0] for area in x['AreaList']]})
	json.dump(j, fout, separators=(', ', ': '), ensure_ascii=False)