import subprocess
  
	

peerids = ['1','2','3','4','5','6']
subprocess.Popen('java dhost/net/StatsKeeper')
for p in peerids:
    
     cmd = 'java dhost/ui/Client -gamestate w -peerlist peerlist2.txt -mode swing -peerid '+p+' -statserver localhost:5500 -simdelay 5 -messagedelay 1'
     subprocess.Popen(cmd)


