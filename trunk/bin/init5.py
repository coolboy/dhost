import subprocess
  
	

peerids = ['1','2','3','4','5','6','7','8','9','10','11','12']
subprocess.Popen('java dhost/net/StatsKeeper')

for p in peerids:
    
     cmd = 'java dhost/ui/Client -gamestate w -peerlist peerlist5.txt -mode swing -peerid '+p+' -statserver localhost:5500 -simdelay 5 -messagedelay 1'
     subprocess.Popen(cmd)


