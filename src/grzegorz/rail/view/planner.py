import urllib2, json, sys
from pprint import pprint

print sys.argv[1]
print sys.argv[2]

data = {'domain': open(sys.argv[1], 'r').read(),
		'problem': open(sys.argv[2], 'r').read()}

req = urllib2.Request('http://solver.planning.domains/solve')
req.add_header('Content-Type', 'application/json')
resp = json.loads(urllib2.urlopen(req, json.dumps(data)).read())

with open(sys.argv[3], 'w') as f:
	pprint(resp)
	if resp['status']=="ok":
		f.write('\n'.join([act['name'] for act in resp['result']['plan']])) 
		with open(sys.argv[3] + "dump", 'w') as d:
			d.write(json.dumps(resp['result']))
	else:
		f.write("NO PLAN")
		with open(sys.argv[3] + "dump", 'w') as d:
			d.write(json.dumps(resp))
