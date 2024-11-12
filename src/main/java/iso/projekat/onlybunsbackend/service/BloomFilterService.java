package iso.projekat.onlybunsbackend.service;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Set;

@Service
public class BloomFilterService {
    private BloomFilter<String> ipBloomFilter;
    private BloomFilter<String> usernameBloomFilter;

    public BloomFilterService() {
        // Inicijalizujemo Bloom filtere
        ipBloomFilter = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8), 1000, 0.01);
        usernameBloomFilter = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8), 1000, 0.01);
    }

    // Dodajemo dozvoljene IP adrese u Bloom filter
    public void addAllowedIps(Set<String> allowedIps) {
        allowedIps.forEach(ipBloomFilter::put);
    }

    // Proveravamo da li je IP adresa dozvoljena
    public boolean isIpAllowed(String ip) {
        return true;
        //return ipBloomFilter.mightContain(ip);
    }

    // Dodajemo korisničko ime u Bloom filter
    public void addUsername(String username) {
        usernameBloomFilter.put(username);
    }

    // Proveravamo da li korisničko ime već postoji
    public boolean isUsernameTaken(String username) {
        return usernameBloomFilter.mightContain(username);
    }
}
