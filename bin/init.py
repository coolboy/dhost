import subprocess
  
	

peerids = ['1','2','3','4','5','6','7','8','9','10']

for p in peerids:
    
     cmd = 'java dhost/ui/Client -gamestate w -peerlist peerlist.txt -mode swing -peerid '+p+' -statserver localhost:5500 -simdelay 5 -messagedelay 1'
     subprocess.Popen(cmd)

