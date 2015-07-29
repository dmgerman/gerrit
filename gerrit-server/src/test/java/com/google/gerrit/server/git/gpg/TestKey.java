begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.server.git.gpg
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
operator|.
name|gpg
package|;
end_package

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|bcpg
operator|.
name|ArmoredInputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPPrivateKey
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPPublicKey
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPPublicKeyRing
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPSecretKey
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPSecretKeyRing
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|operator
operator|.
name|bc
operator|.
name|BcKeyFingerprintCalculator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|operator
operator|.
name|bc
operator|.
name|BcPBESecretKeyDecryptorBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|operator
operator|.
name|bc
operator|.
name|BcPGPDigestCalculatorProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_class
DECL|class|TestKey
class|class
name|TestKey
block|{
comment|/**    * A valid key with no expiration.    *    *<pre>    * pub   2048R/46328A8C 2015-07-08    *       Key fingerprint = 04AE A7ED 2F82 1133 E5B1  28D1 ED06 25DC 4632 8A8C    * uid                  Testuser One&lt;test1@example.com&gt;    * sub   2048R/F0AF69C0 2015-07-08    *</pre>    */
DECL|method|key1 ()
specifier|static
name|TestKey
name|key1
parameter_list|()
throws|throws
name|PGPException
throws|,
name|IOException
block|{
return|return
operator|new
name|TestKey
argument_list|(
literal|"-----BEGIN PGP PUBLIC KEY BLOCK-----\n"
operator|+
literal|"Version: GnuPG v1\n"
operator|+
literal|"\n"
operator|+
literal|"mQENBFWdTIkBCADOaygDKjLuRX6LXAvBAYB91cmTf1MSlmEy+qsG3c9ijjQixPkr\n"
operator|+
literal|"atdYkocrrT2S0R9UGjksTOI2WN5S0lQfLA1RSk63KURQE+OF+IfNqdD6nQdLBs1w\n"
operator|+
literal|"va+GDj/uvuI05I0oXf/M7POdFphutrS4EUDBnFPj6ns/0C2sTRTxliD+Y9Y9a84V\n"
operator|+
literal|"DfVVUbJB6wc3LP3L6ImT+cSM7dLq3hZHya+9FNeYPmPYnBrkJyqf2NDd38Sddsro\n"
operator|+
literal|"7smw/GgCZHnnuVNS4C7NsHr6900VKC+JDtdx+fqptixcAEJWiGoQfWqU+hYmia3p\n"
operator|+
literal|"9+Xw02+3FcjOT6ONUCmHX+xlz0pXW4iIYlPpABEBAAG0IFRlc3R1c2VyIE9uZSA8\n"
operator|+
literal|"dGVzdDFAZXhhbXBsZS5jb20+iQE4BBMBAgAiBQJVnUyJAhsDBgsJCAcDAgYVCAIJ\n"
operator|+
literal|"CgsEFgIDAQIeAQIXgAAKCRDtBiXcRjKKjHblB/9RaFO5+GTDIphAL/aVj2u+d8Lq\n"
operator|+
literal|"yUpBrDp3P06QDGpKGFMAovBuh+NLH76VKNIzQLQC8rdTj651fLcLMuJ1enQ3Rblg\n"
operator|+
literal|"RKr1oc+wqqtFHr4QyOQjE/N3C9GQjEzfqn4qnp5KtZxYFnlvU5NGehid7M1HTZMx\n"
operator|+
literal|"jRcHbM9KQnsE5Z4fh4wmN5ynG+5nbaF4O9otPOpFzYRvIhxFmHscWyOgRaMZiYEX\n"
operator|+
literal|"7Qkzze+scAlc9E/EWRJQIFcxnxV/SYIT4qCTT1g2aKA8OCBO/ZTOleH8SzvTODjy\n"
operator|+
literal|"W0lGHnh/ZqH6XGVcGUaJZZ2uHTck1+czuVVShNcXPW1W20T6E9UqzHbJHN0guQEN\n"
operator|+
literal|"BFWdTIkBCACoLVdPr3gpQwzI+2NGXjdtoyqYoPlgfeyI2M1XQD/7+rLZTbi14ZjN\n"
operator|+
literal|"vYkS/+/oGtVEmiYOiAVTwmkjCYkKGDgNcCiJVekiPAN6JryVv488wRc999b5LpFE\n"
operator|+
literal|"fhLGwI0YxjcS4KFFnpMC3wSb6tJUnHRLVoE5d8icdiaOpgYdp7uqWkSx2oxqHgIb\n"
operator|+
literal|"nuyrk3ydEcS4ZeGD+w+taIxMc9F1DS9kiXALD7xWgUkmqZLEQoNgF6KlwCHXRd3m\n"
operator|+
literal|"rBCo97sE95yKcq98ZMIWuQtTcEccZsN/6jlsei+9RI0tqs+FbZnIFm/go9zk11Vl\n"
operator|+
literal|"IQ9QFSj6ruqoKrYvNZuDDLD1lHvZPD4/ABEBAAGJAR8EGAECAAkFAlWdTIkCGwwA\n"
operator|+
literal|"CgkQ7QYl3EYyiox+HAf/Z/OCQO3jxALAcn3oUb1g/IlHm6qZv7RJOFUsj/16fGiF\n"
operator|+
literal|"rRTP15zMXzyqV+L/LGV/owvOsdD/o7boZz4C/U98COx0Nl1jOrmPATOl+xqsgpEj\n"
operator|+
literal|"Fhk+eAR7exO2XxW+u2g4cYoSMosIOX5w1GrdsxQeaZDwiSJMEOR2cVLs3YI19Ci/\n"
operator|+
literal|"FuzActZ0wJNk0nlNF6l8CAbzwN6pM9OIc/iBIwDjz92KUco0NF8XKZnxqhH4wfHB\n"
operator|+
literal|"PGkTx8RwOvELUTDMtvYnG5R0QtND0RbOnmp4ZVZmeOjKSLo1mZliUZB1H2PPSxrA\n"
operator|+
literal|"0oLr8+wLntz1SU7uS4ddvhSQW+j2M/0pa352KUwmrw==\n"
operator|+
literal|"=o/aU\n"
operator|+
literal|"-----END PGP PUBLIC KEY BLOCK-----\n"
argument_list|,
literal|"-----BEGIN PGP PRIVATE KEY BLOCK-----\n"
operator|+
literal|"Version: GnuPG v1\n"
operator|+
literal|"\n"
operator|+
literal|"lQOYBFWdTIkBCADOaygDKjLuRX6LXAvBAYB91cmTf1MSlmEy+qsG3c9ijjQixPkr\n"
operator|+
literal|"atdYkocrrT2S0R9UGjksTOI2WN5S0lQfLA1RSk63KURQE+OF+IfNqdD6nQdLBs1w\n"
operator|+
literal|"va+GDj/uvuI05I0oXf/M7POdFphutrS4EUDBnFPj6ns/0C2sTRTxliD+Y9Y9a84V\n"
operator|+
literal|"DfVVUbJB6wc3LP3L6ImT+cSM7dLq3hZHya+9FNeYPmPYnBrkJyqf2NDd38Sddsro\n"
operator|+
literal|"7smw/GgCZHnnuVNS4C7NsHr6900VKC+JDtdx+fqptixcAEJWiGoQfWqU+hYmia3p\n"
operator|+
literal|"9+Xw02+3FcjOT6ONUCmHX+xlz0pXW4iIYlPpABEBAAEAB/wLoOXEJ+Buo+OZHjpb\n"
operator|+
literal|"SSZf8GdGs+mOJoKbSJvR6zT/rFsrikUvOPmgt8B9qWjKmJVXO5L09+/Wd/MuX0L1\n"
operator|+
literal|"7plhdvowP1bl2/j5VyLvZx2qwKXkiCGStFzrBGp9nKtJp4Z8O69pb//ZXaiAtDJC\n"
operator|+
literal|"HFa1kYT4VgFTevrXtg/z/C0np4Yjx0mZpw4nfISEeHCiYCyRa/B8R1+Pc4uIcoSo\n"
operator|+
literal|"G3aq6Ow9m/LGvw0MRO5qHvqoF41TLPQpGKjKEsCBKHF1qh0tOOUHnLGrvbmdFnGr\n"
operator|+
literal|"UXJpRkLdRTnj8ufvA4XVZhImzL+lD+ALtjlV14xh8nsNKYL42880GFl5Cl0OtBcE\n"
operator|+
literal|"lgQBBADPJ6kHdvUYOe0zugRdukBSYLkZcYwRiphom7dZuavYICIu6B14ljEONzVD\n"
operator|+
literal|"mPhi2lDOawZOURKwYd9S4K11XWLsTYe7XEwkc+1Fpvu4L/JqnJTTnnvbx05ZsqD5\n"
operator|+
literal|"j9tybPlrTuLrf2ctfcC03Z55wfo6azsbf89yrr6QX0+l9dlkYQQA/xcMdQJ0Z5vm\n"
operator|+
literal|"kvyaCPsQzJc/8noVO9PMv7xJm14gJWK7Px3y2eBidzpCbVVFnGWW6CPb3qKerB5U\n"
operator|+
literal|"pwcF4gCFWyP9C2YtnB0hgqixIPfR+UO8gpqdY6MP8NPspoXouffRn+Zic/P6Cxje\n"
operator|+
literal|"/MGxNQBeRtqb2IGh1xZ8v/8tmmmxHIkEAP74HkGETcXmlj3/6RlwTBUAovPARSn7\n"
operator|+
literal|"LDtOCPezg6mQmble1BvnTnAwOHKJVqjx+3qsGqMe8OGGXAxZPSU1xSmOShBFrpDp\n"
operator|+
literal|"xArE67arE17pT1lyD/gmHRuqnNMvgRrwz1mDm3G2ohWkCVixEiB+8vPQfbZrJBgQ\n"
operator|+
literal|"WxOF4RCo2WWyRKa0IFRlc3R1c2VyIE9uZSA8dGVzdDFAZXhhbXBsZS5jb20+iQE4\n"
operator|+
literal|"BBMBAgAiBQJVnUyJAhsDBgsJCAcDAgYVCAIJCgsEFgIDAQIeAQIXgAAKCRDtBiXc\n"
operator|+
literal|"RjKKjHblB/9RaFO5+GTDIphAL/aVj2u+d8LqyUpBrDp3P06QDGpKGFMAovBuh+NL\n"
operator|+
literal|"H76VKNIzQLQC8rdTj651fLcLMuJ1enQ3RblgRKr1oc+wqqtFHr4QyOQjE/N3C9GQ\n"
operator|+
literal|"jEzfqn4qnp5KtZxYFnlvU5NGehid7M1HTZMxjRcHbM9KQnsE5Z4fh4wmN5ynG+5n\n"
operator|+
literal|"baF4O9otPOpFzYRvIhxFmHscWyOgRaMZiYEX7Qkzze+scAlc9E/EWRJQIFcxnxV/\n"
operator|+
literal|"SYIT4qCTT1g2aKA8OCBO/ZTOleH8SzvTODjyW0lGHnh/ZqH6XGVcGUaJZZ2uHTck\n"
operator|+
literal|"1+czuVVShNcXPW1W20T6E9UqzHbJHN0gnQOYBFWdTIkBCACoLVdPr3gpQwzI+2NG\n"
operator|+
literal|"XjdtoyqYoPlgfeyI2M1XQD/7+rLZTbi14ZjNvYkS/+/oGtVEmiYOiAVTwmkjCYkK\n"
operator|+
literal|"GDgNcCiJVekiPAN6JryVv488wRc999b5LpFEfhLGwI0YxjcS4KFFnpMC3wSb6tJU\n"
operator|+
literal|"nHRLVoE5d8icdiaOpgYdp7uqWkSx2oxqHgIbnuyrk3ydEcS4ZeGD+w+taIxMc9F1\n"
operator|+
literal|"DS9kiXALD7xWgUkmqZLEQoNgF6KlwCHXRd3mrBCo97sE95yKcq98ZMIWuQtTcEcc\n"
operator|+
literal|"ZsN/6jlsei+9RI0tqs+FbZnIFm/go9zk11VlIQ9QFSj6ruqoKrYvNZuDDLD1lHvZ\n"
operator|+
literal|"PD4/ABEBAAEAB/4kQnJauehcbRpqktjaqSGmP9HFSp+50CyZbLUJJM8m0uyQsZMr\n"
operator|+
literal|"k9JQOZc+Q3RERNTKj7m41Fbhsj7c0Qd856/eJdp3kdBME0hko8lxN/X4EWGjeLYe\n"
operator|+
literal|"z41+iPgfZhCF0Oa66TecPQ5RRihGPaDPoVPpkmMWMt9L7KVviBg1eJ6bobVIY5hu\n"
operator|+
literal|"a7KFJHZQcCI1OvdJ0cx89KDSbnH8iMM6Kmw1bE3D2FEaWctuKLBo5PNRgyTJvdBd\n"
operator|+
literal|"PSf56/Rc6csPqmOntQi2Yn8n47eCOTclHNuygSTJeHPpymVuWbhMq6fhJat/xA+V\n"
operator|+
literal|"kyT8I2c45RQb0dKId+wEytjbKw8AI6Q3GXqhBADOhsr9M+JWc4MpD43mCDZACN4v\n"
operator|+
literal|"RBRxSrJvO/V6HqQPmKYRmr9Gk3vxgF0zCf5zB1QeBiXpTpShxV87RIbUYReOyavp\n"
operator|+
literal|"87zH6/SkRxQJiBEpQh5Fu5CoAaxGOivxbPqdWHrBY6jvqkrRoMPNiFJ6/ty5w9jx\n"
operator|+
literal|"i9kGm9PelQGu2SdLNwQA0HbGo8sC8h5TSTEDCkFHRYzVYONx+32AlkCsJX9mEt0E\n"
operator|+
literal|"nG8d97Ay24JsbnuXSq04FJrqzjOVyHLUffpXnAGELJZVNCIparSyqIaj43UG/oPc\n"
operator|+
literal|"ICPmR7zI9G49ICUPSzI7+S2+BwjbiHRQcP0zmxbH92G4abYwKfk7dsDpGyVM+TkD\n"
operator|+
literal|"/2nUiV0CRqnGipeiLWNjW/Md0ufkwqBvCWxrtxj0rQCyvBOVg3B6DocVNzgOOYa1\n"
operator|+
literal|"ji3We5A9mSP40JBmMfk2veFrDdsGn4G+OpzMxKQtNfYemqjALfZ2zTdax0mXPXy6\n"
operator|+
literal|"Gl0jUgSGrxGm8QnRLsrRx7G7ZKnvkcS+YsdQ8dbtzvJtQfiJAR8EGAECAAkFAlWd\n"
operator|+
literal|"TIkCGwwACgkQ7QYl3EYyiox+HAf/Z/OCQO3jxALAcn3oUb1g/IlHm6qZv7RJOFUs\n"
operator|+
literal|"j/16fGiFrRTP15zMXzyqV+L/LGV/owvOsdD/o7boZz4C/U98COx0Nl1jOrmPATOl\n"
operator|+
literal|"+xqsgpEjFhk+eAR7exO2XxW+u2g4cYoSMosIOX5w1GrdsxQeaZDwiSJMEOR2cVLs\n"
operator|+
literal|"3YI19Ci/FuzActZ0wJNk0nlNF6l8CAbzwN6pM9OIc/iBIwDjz92KUco0NF8XKZnx\n"
operator|+
literal|"qhH4wfHBPGkTx8RwOvELUTDMtvYnG5R0QtND0RbOnmp4ZVZmeOjKSLo1mZliUZB1\n"
operator|+
literal|"H2PPSxrA0oLr8+wLntz1SU7uS4ddvhSQW+j2M/0pa352KUwmrw==\n"
operator|+
literal|"=MuAn\n"
operator|+
literal|"-----END PGP PRIVATE KEY BLOCK-----\n"
argument_list|)
return|;
block|}
comment|/**    * A valid key expiring in 2065.    *    *<pre>    * pub   2048R/378A0AED 2015-07-08 [expires: 2065-06-25]    *       Key fingerprint = C378 369A CBCD 34CC 138D  90B1 4531 1A6F 378A 0AED    * uid                  Testuser Two&lt;test2@example.com&gt;    * sub   2048R/46D4F204 2015-07-08 [expires: 2065-06-25]    *</pre>    */
DECL|method|key2 ()
specifier|static
specifier|final
name|TestKey
name|key2
parameter_list|()
throws|throws
name|PGPException
throws|,
name|IOException
block|{
return|return
operator|new
name|TestKey
argument_list|(
literal|"-----BEGIN PGP PUBLIC KEY BLOCK-----\n"
operator|+
literal|"Version: GnuPG v1\n"
operator|+
literal|"\n"
operator|+
literal|"mQENBFWdTP8BCADRxNpasIv0jtNXTK6VYIS2VJ2Xk0ZD6gtxeoXCpjQ+TsB9fxh3\n"
operator|+
literal|"vAMPt2Zu5LqoGwygKOJj1zquG8xk7GUCCHJk3+qG8xxB1xGtSz2vLyfRm7fOZmHj\n"
operator|+
literal|"3W/C/25lynSPDrfvcwvwA4PN8iP5EWbWU10L6WOZGMwwwtVDUSEouSOw2LEepxLV\n"
operator|+
literal|"rkKuZcyHaivheDbUlZliwe9rGXd4hh1h4qyNQWG3q+ytlL28sVkOzUh6IMBTvqhe\n"
operator|+
literal|"IRsvxvaVSLV8jRVKfUTqw0g57ft4ZD2/L46yUTXzr9aUCBjTNxvWLlyboqql/D8P\n"
operator|+
literal|"inp51h3cvAg7NW5RdG1GEYmylH8SygT5utPxABEBAAG0IFRlc3R1c2VyIFR3byA8\n"
operator|+
literal|"dGVzdDJAZXhhbXBsZS5jb20+iQE+BBMBAgAoBQJVnUz/AhsDBQld/A8ABgsJCAcD\n"
operator|+
literal|"AgYVCAIJCgsEFgIDAQIeAQIXgAAKCRBFMRpvN4oK7UZqCACWwQL/YvBK4b0m+R0d\n"
operator|+
literal|"UdvAXeBx7DwOAnAodis9ZVqChb7RxcZQxF1Ti9mtCBPPQGuEs5wE2Ocrrq+L13r6\n"
operator|+
literal|"bgW+1WOB1tZSDVxwL1PnZFw/SyADRIDCZrOHiAkp82UnZwWAkk39GzNJtt1wTYDZ\n"
operator|+
literal|"FMTFUr2SPscXk1k7muS+ZfEFwNPD4tODo/poJKDYEJ80Z5UXXFQLDtsfdeIXMFIT\n"
operator|+
literal|"449CYoq8XBMBfvyWl/LLpw0r3JI6pV/YdH3Oeuz8XkkEVzRxaxB6Zmeo5jSwjR/T\n"
operator|+
literal|"8TKDGwwiuwiiT3SfkFSVdcjKulRuXSRNs1Ouf7/UC3cq4bG2WXWa85X1+HQRm7iu\n"
operator|+
literal|"RHSOuQENBFWdTP8BCADhhGxAA0pX5yBHwIgM1j0gw2h5nSsopDrO6t/sbRUcNxnR\n"
operator|+
literal|"tBScgKZnP0sjRTYEUIwmZuseHMBohtVCuMaDt06qyZDvDk/98j3AeE5t2dgFnOIe\n"
operator|+
literal|"qCrm/6aejbFcQOpxe6U29KJRCAxuwNtB15X1VH1Kj7B0gRSTu13n/5sUsi2lunoZ\n"
operator|+
literal|"oIvpIe9tZH4aXitCY2MCQH+hTyCyNBzlEa44kWz6LxUsPdo7I6rXkTr6Ot7wQh+9\n"
operator|+
literal|"7HCe042GIq65h0apgujyjhJidjch5ur1mngaSNSEyvbji2MGC+cd3wAIstG5a7xP\n"
operator|+
literal|"d9MncY5Q/eH+hn96694k5bckottSyGm/3f2Ihfj1ABEBAAGJASUEGAECAA8FAlWd\n"
operator|+
literal|"TP8CGwwFCV38DwAACgkQRTEabzeKCu1FNwgAif4eK2v7R3QubL2S6wmb1nsgRMgV\n"
operator|+
literal|"YoxGBeUk2EK6WZ5IPor93ySd0ixRVNMRmJ8BLH3EMjZQTzkDG+BH6zFyxo6lLHw9\n"
operator|+
literal|"NxQjI06tqQWgyyK0mEweVwB/zqtxiB4lNUpsNbqOZWnBJ3d6o1SsnD2Q3uwvP5fb\n"
operator|+
literal|"fSIgdmUk3c0VMdgA+KzWjPD/PJIPujE+ckHhjn5cbDNw35/FuyhkLJfqlOG7SPvM\n"
operator|+
literal|"NmCdJ1Pcqju9t7sf6b0BGPDOCL4gpuWKK7HJz9WxngNb3FSziLbyPLk13ynADO+v\n"
operator|+
literal|"EOR44LPyXE9kVxPusazsXlt9ayTOhELhwzw7sGFFu8E17Cpn7GnVj3tN9A==\n"
operator|+
literal|"=1e/A\n"
operator|+
literal|"-----END PGP PUBLIC KEY BLOCK-----\n"
argument_list|,
literal|"-----BEGIN PGP PRIVATE KEY BLOCK-----\n"
operator|+
literal|"Version: GnuPG v1\n"
operator|+
literal|"\n"
operator|+
literal|"lQOYBFWdTP8BCADRxNpasIv0jtNXTK6VYIS2VJ2Xk0ZD6gtxeoXCpjQ+TsB9fxh3\n"
operator|+
literal|"vAMPt2Zu5LqoGwygKOJj1zquG8xk7GUCCHJk3+qG8xxB1xGtSz2vLyfRm7fOZmHj\n"
operator|+
literal|"3W/C/25lynSPDrfvcwvwA4PN8iP5EWbWU10L6WOZGMwwwtVDUSEouSOw2LEepxLV\n"
operator|+
literal|"rkKuZcyHaivheDbUlZliwe9rGXd4hh1h4qyNQWG3q+ytlL28sVkOzUh6IMBTvqhe\n"
operator|+
literal|"IRsvxvaVSLV8jRVKfUTqw0g57ft4ZD2/L46yUTXzr9aUCBjTNxvWLlyboqql/D8P\n"
operator|+
literal|"inp51h3cvAg7NW5RdG1GEYmylH8SygT5utPxABEBAAEAB/0WW33OVqzEBwj9b/3X\n"
operator|+
literal|"i+75I/Gb+yVtDZ/km2NwSJie33PirE4mTNKitTBkt1oxmphw5Yqji4gEkI/rXcqy\n"
operator|+
literal|"OcY/fCIZ+gVT+yE2MCPF7Se4Tnl7tSvPxoUn6mOQ09AygyYVjlSCY02EAL/WxwUH\n"
operator|+
literal|"6OCs6VYlNiBlPg7O2vHGzlzAd1aMmlG3ytlhb0SIbilaJn/wlQ2SEGySjIAP1qRH\n"
operator|+
literal|"UXsTfW7oAjdqAY1CbCWg/0FnMBF+DnChH634dbLrS2OefcB70l61trEfRcHbMNTv\n"
operator|+
literal|"9nVxDDCpaIdxsOfgWpe0GMG1qddRAxBIOVjNUFOL22xEFyaXnt/uagUtKQ7yejci\n"
operator|+
literal|"bgTFBADcuhsfQaBX1G095iG2qr8Rx2T5GqNf9oZA+rbweWegqIH7MUXHI1KKwwJx\n"
operator|+
literal|"C+rR5AgnxTSP614XI/AWB/txdelm8z0jLobpS6B1vzM2vRQ7hpwjJ3UvUkoQ5uYL\n"
operator|+
literal|"DjaBqQi0w1cPJA79H0Yujc1zgdhATymz0uDL1BC2bHLIMuhelwQA80p07G1w8HLQ\n"
operator|+
literal|"bTdgNwtDBMKIw39/ZyQy8ppxmpD4J6zf25r95g3er0r+njrHsa+72LnvexbedpKA\n"
operator|+
literal|"4eiDJPN+l5jJOEWfL2WtGcqJ01bdFBPcl73tuwDJJtieUlKZH0jRjykuuUX8F+tJ\n"
operator|+
literal|"yrmVoIGtawoeLKq3hMMOK4xi+sh3OrcD+wXIU24eO3YfUde5bhyaQplNMU5smIU0\n"
operator|+
literal|"+looOEmFsZcTONgoN+FKrnm2TY9d4FHZ+QgtnksWHmmLxQJPtp9rHJ5BgdxMBPcK\n"
operator|+
literal|"3w5GXRuWlOmqmnAb6vp0Q0yzVDLKCcwba0S23m3tbjZsLDcI7MG/knsp9gtL676D\n"
operator|+
literal|"AsrpeF2+Apj0OwG0IFRlc3R1c2VyIFR3byA8dGVzdDJAZXhhbXBsZS5jb20+iQE+\n"
operator|+
literal|"BBMBAgAoBQJVnUz/AhsDBQld/A8ABgsJCAcDAgYVCAIJCgsEFgIDAQIeAQIXgAAK\n"
operator|+
literal|"CRBFMRpvN4oK7UZqCACWwQL/YvBK4b0m+R0dUdvAXeBx7DwOAnAodis9ZVqChb7R\n"
operator|+
literal|"xcZQxF1Ti9mtCBPPQGuEs5wE2Ocrrq+L13r6bgW+1WOB1tZSDVxwL1PnZFw/SyAD\n"
operator|+
literal|"RIDCZrOHiAkp82UnZwWAkk39GzNJtt1wTYDZFMTFUr2SPscXk1k7muS+ZfEFwNPD\n"
operator|+
literal|"4tODo/poJKDYEJ80Z5UXXFQLDtsfdeIXMFIT449CYoq8XBMBfvyWl/LLpw0r3JI6\n"
operator|+
literal|"pV/YdH3Oeuz8XkkEVzRxaxB6Zmeo5jSwjR/T8TKDGwwiuwiiT3SfkFSVdcjKulRu\n"
operator|+
literal|"XSRNs1Ouf7/UC3cq4bG2WXWa85X1+HQRm7iuRHSOnQOYBFWdTP8BCADhhGxAA0pX\n"
operator|+
literal|"5yBHwIgM1j0gw2h5nSsopDrO6t/sbRUcNxnRtBScgKZnP0sjRTYEUIwmZuseHMBo\n"
operator|+
literal|"htVCuMaDt06qyZDvDk/98j3AeE5t2dgFnOIeqCrm/6aejbFcQOpxe6U29KJRCAxu\n"
operator|+
literal|"wNtB15X1VH1Kj7B0gRSTu13n/5sUsi2lunoZoIvpIe9tZH4aXitCY2MCQH+hTyCy\n"
operator|+
literal|"NBzlEa44kWz6LxUsPdo7I6rXkTr6Ot7wQh+97HCe042GIq65h0apgujyjhJidjch\n"
operator|+
literal|"5ur1mngaSNSEyvbji2MGC+cd3wAIstG5a7xPd9MncY5Q/eH+hn96694k5bckottS\n"
operator|+
literal|"yGm/3f2Ihfj1ABEBAAEAB/wP5H+mcTTrhe+57sEHuo9bQDocG+3fMtesHlRCept6\n"
operator|+
literal|"vg1VQG4Va2GOtCCs7yMz4aNGz4jxOdB7bUkZJyFiRehG0+ahWi5b9JbSegf46Nm2\n"
operator|+
literal|"54vt4icH2WtaEB04JaD/91k4yrunnzwVEAVDmhhIzjf4KbEjPLeBA7rF7zb0Gexq\n"
operator|+
literal|"mdxEGO/6KdeQ6KOxkpWEqIIdl/mAGsYCprHeKL/XL+KXYr92nEbUcltmt59TTnoo\n"
operator|+
literal|"00BQCPuHCdpcUd5nuaxpCZLM+BEpxtj0sinz0ofuWU9RI4K00R01MKXWMucdOhTZ\n"
operator|+
literal|"kUy5dMx8wA07xbjkE/nH86N76Mty133OB7G3lBBDfO4PBADulfLzbjXUnS1kTKeP\n"
operator|+
literal|"j/HF1E9qafzTDS/QD55OVajDq66A6zaOazKbURHNZmIqpLO4715+iNtrZQUEP3e1\n"
operator|+
literal|"mwngeizvAv9luA9kJ1YDTCfsS5H5cYzavhfwuqBu7fQBm/PQqZplQuPCxgXEIBaY\n"
operator|+
literal|"M0uvR0I/FSwFrepRN2IA6dAkrwQA8fpJEg8C9OLFzDf0rxV3eWwEelemN4E50Obu\n"
operator|+
literal|"nxtg9IJWZ+QIWkRVLJ8if5+p85s2ieCw8hzEF0FyNfWUnfW5eoN4/j50loR4EbZS\n"
operator|+
literal|"qOpUJGwr8ezyQN8PpduDOe9OQnUYAv9FY9Rk46L4937GDF2w5gdxyNdKO8yG+Z3A\n"
operator|+
literal|"6/0DLZsEAOQsRUXIl1XLjkdugfFQ8V9Fv3AYWJt+8zknwcQ+Z3uOtyY2muCi9hX2\n"
operator|+
literal|"BtuPojjwmN6x8wntMaUkzYHVSdz/cdx+na7VNS2kZHfnECWZGR6IHyRTJN5612yi\n"
operator|+
literal|"e4MIdTE+BgL1HPq+VIPlMBehEksC5qM0WSq8baMsacGMYeAL8ntoRuyJASUEGAEC\n"
operator|+
literal|"AA8FAlWdTP8CGwwFCV38DwAACgkQRTEabzeKCu1FNwgAif4eK2v7R3QubL2S6wmb\n"
operator|+
literal|"1nsgRMgVYoxGBeUk2EK6WZ5IPor93ySd0ixRVNMRmJ8BLH3EMjZQTzkDG+BH6zFy\n"
operator|+
literal|"xo6lLHw9NxQjI06tqQWgyyK0mEweVwB/zqtxiB4lNUpsNbqOZWnBJ3d6o1SsnD2Q\n"
operator|+
literal|"3uwvP5fbfSIgdmUk3c0VMdgA+KzWjPD/PJIPujE+ckHhjn5cbDNw35/FuyhkLJfq\n"
operator|+
literal|"lOG7SPvMNmCdJ1Pcqju9t7sf6b0BGPDOCL4gpuWKK7HJz9WxngNb3FSziLbyPLk1\n"
operator|+
literal|"3ynADO+vEOR44LPyXE9kVxPusazsXlt9ayTOhELhwzw7sGFFu8E17Cpn7GnVj3tN\n"
operator|+
literal|"9A==\n"
operator|+
literal|"=qbV3\n"
operator|+
literal|"-----END PGP PRIVATE KEY BLOCK-----\n"
argument_list|)
return|;
block|}
comment|/**    * A key that expired in 2006.    *    *<pre>    * pub   2048R/17DE1ACD 2005-07-08 [expired: 2006-07-08]    *       Key fingerprint = 1D9E EB79 DD38 B049 939D  9CAF 3CEC 781B 17DE 1ACD    * uid                  Testuser Three&lt;test3@example.com&gt;    *</pre>    */
DECL|method|key3 ()
specifier|static
specifier|final
name|TestKey
name|key3
parameter_list|()
throws|throws
name|PGPException
throws|,
name|IOException
block|{
return|return
operator|new
name|TestKey
argument_list|(
literal|"-----BEGIN PGP PUBLIC KEY BLOCK-----\n"
operator|+
literal|"Version: GnuPG v1\n"
operator|+
literal|"\n"
operator|+
literal|"mQENBELOp0UBCACxholOPWuKhK+TYb88nvLUSCMvTLIFEpb5u3Eavr0wiluEzq6H\n"
operator|+
literal|"55nswAD3dQm8DWxA7yUlEYjPr5btpw7V9441bb1+qtgZMJ10RTdEb/WjyctdGA99\n"
operator|+
literal|"uOKBEarWbt8W+w6lyJ9NXy5bS/x5EwHHfoTFp4ff6ffHI5hbx1a00K8oxmitgd0X\n"
operator|+
literal|"Mx86UmauFNJYupZOZG9gEcP4RbRp7e2pm4Jy1WLEOeg9Fdgm5e5Hj2nMkCSZ9BKV\n"
operator|+
literal|"cxuOllSVzM/Zp0/4+RS9R57jKo3/V74Whwh9yQNgL9UxdNk7L0eGqvaT3EjXxjOc\n"
operator|+
literal|"RCeJiucGN/0W2iq+V01/QGspp4SKtAogWBozABEBAAG0IlRlc3R1c2VyIFRocmVl\n"
operator|+
literal|"IDx0ZXN0M0BleGFtcGxlLmNvbT6JAT4EEwECACgFAkLOp0UCGwMFCQHhM4AGCwkI\n"
operator|+
literal|"BwMCBhUIAgkKCwQWAgMBAh4BAheAAAoJEDzseBsX3hrNYg0H/2CMm5/JDQNSuRFC\n"
operator|+
literal|"ECWLrcOeimuvwbmkonNzOkvKbGXl73GStISAksRWAHBQED1rEPC0NkFCDeVZO7df\n"
operator|+
literal|"SYLlsqKwV6uSh05Ra0F5XeniC12YpAyzoQyCGRS2wLaS822j0zUPXA8XLaO2blCu\n"
operator|+
literal|"R+8sNu/oecMRcFK4S9NaApi3vdqBNhLiN1/Lpqn1LfB8uIO+eaUf4PmCWbaPgzSk\n"
operator|+
literal|"qcPfKZmocNXdgLV5Q80n3hc2y2nrl+vDW2M+eVZuDHAok2BOD9uGKFfLAbaXLbX5\n"
operator|+
literal|"btBW2L0UHtoEyiqkRfD6lX2laSLQmA6+eup7e4GS+s0vXBuVh8XEYddV6Yjt8H7/\n"
operator|+
literal|"2thO41K5AQ0EQs6nRQEIAM/833UHK1DuFlOm7/n18dRMvs7BkXvg+hPquKWMG3be\n"
operator|+
literal|"eE4sh1NG5DbRCdo6iacZLarWr3FDz7J9+wswRhtHCh3pGHEuaJk52vRjQxlkNh5F\n"
operator|+
literal|"p5u2R4WF546bWqX45xPdLfHVTPyWB9q7aVxE+6Q+MHa6lMoyTVnTVCOy3nshiihw\n"
operator|+
literal|"dxLsxaga+QmaL0bAR+dRcO6ucj7TDQXz1AJAVp26c0LXV9iErhFuuybUZKT0a9Aj\n"
operator|+
literal|"FoumMZ6l+k30sSdjSjpBMsNvPos0dTPPRXUMu77o5sj+pHa4o8WctgB3o7BHQELp\n"
operator|+
literal|"KgujZ2sKC9Nm395u6Q4cqUWihzb/Y7rIRuNHJarI7vUAEQEAAYkBJQQYAQIADwUC\n"
operator|+
literal|"Qs6nRQIbDAUJAeEzgAAKCRA87HgbF94azRiBB/4vAyOOjUjK3lDWjHGs7mvEWJI/\n"
operator|+
literal|"1MeLlGPswCSInJBa+HMiMI4tzq+hu5ejGThojNbmnL96GdzfDkMlP4Feyxb2rjtb\n"
operator|+
literal|"NrD/R5tlXHmjX/QLzep4LCeMziP80fu8qUeiOej/Ecdny0w365PlMdt10RaYR8VE\n"
operator|+
literal|"ZX/DAie6JfElnfQcG5q8TIOH3i71qxV+kIoPqKWfQ0MXrNEJ3BYFfDGdUt8U1Kq9\n"
operator|+
literal|"OuIHVRgGS7mMSyjgNqqp7MBeMY+PFFZaZel5yoYVjb9d3L8XvVv2eoa/jPj5FUEU\n"
operator|+
literal|"kE9uxNmwaD1PiV8DvBTYI+eQL4qzfu+3NTG2SfgQYtj5oiGHw8aL3U6QHDJb\n"
operator|+
literal|"=d/Xp\n"
operator|+
literal|"-----END PGP PUBLIC KEY BLOCK-----\n"
argument_list|,
literal|"-----BEGIN PGP PRIVATE KEY BLOCK-----\n"
operator|+
literal|"Version: GnuPG v1\n"
operator|+
literal|"\n"
operator|+
literal|"lQOYBELOp0UBCACxholOPWuKhK+TYb88nvLUSCMvTLIFEpb5u3Eavr0wiluEzq6H\n"
operator|+
literal|"55nswAD3dQm8DWxA7yUlEYjPr5btpw7V9441bb1+qtgZMJ10RTdEb/WjyctdGA99\n"
operator|+
literal|"uOKBEarWbt8W+w6lyJ9NXy5bS/x5EwHHfoTFp4ff6ffHI5hbx1a00K8oxmitgd0X\n"
operator|+
literal|"Mx86UmauFNJYupZOZG9gEcP4RbRp7e2pm4Jy1WLEOeg9Fdgm5e5Hj2nMkCSZ9BKV\n"
operator|+
literal|"cxuOllSVzM/Zp0/4+RS9R57jKo3/V74Whwh9yQNgL9UxdNk7L0eGqvaT3EjXxjOc\n"
operator|+
literal|"RCeJiucGN/0W2iq+V01/QGspp4SKtAogWBozABEBAAEAB/4hGI3ckkLMTjRVa7G1\n"
operator|+
literal|"YYSv4sr8dHXz0CVpZXKOo+Stef3Z4pZTK/BcXOdROvaXooD+EheAs6Yn4fpnT+/K\n"
operator|+
literal|"IB7ZAx6C0OL8vz17gbPuBFltMZ/COUwaCi/gFCUfWQgqRp/SdHaOfCIuTxpAkDSS\n"
operator|+
literal|"tpmWJ8eDDSFudMpgweb+SrF9DkCwp+FgUbzDRzO1aqzuu8PGihCHQt/pkhNHQ63/\n"
operator|+
literal|"srDDqk6lIxxZHhv9+ucr3plDuijkvAa5/QDudQlucKDLtTPSD40UcqYnpg/V/RJU\n"
operator|+
literal|"eBK0ZXmCIHpG9beHW/xdlwrK3eY4Z2sVDMm9TeeHmRYOCr5wQCyeLpMdAt0Ijk6a\n"
operator|+
literal|"nINhBADI2lRodgnLvUKbOvVocz8WQjG1IXlL8iXSNuuHONijPXZiWh7XdkNxr9fm\n"
operator|+
literal|"jRqzvZzYsWGT6MnirX2eXaEWJsWJHxTxJuiuOk0V/iGnV/d+jFduoKXNmB5k/ZB3\n"
operator|+
literal|"6zySi7+STKNyIvnMATVsRoI/cNUwfmx53m6trFg581CnSiA82QQA4kSPw9OXmTKj\n"
operator|+
literal|"ctlHrWsapWu+66pDVZw62lW6lvrd7t+m8liNb6VJuTnwIKVXJOQtUo1+GSMs0+YK\n"
operator|+
literal|"wnd9FGq4jT8l0qBO4K/8B1HxppLC2S0ntC+CusxWMUDbdC2xg+G2W3oLwq3iamgz\n"
operator|+
literal|"LvPTy1Pzs9PqDd6FXIdzieFy6J8W1+sEAKS3vjh7Z/PIVULZhdaohAd5Igd67S/Z\n"
operator|+
literal|"BMWYNbBuJTnnb7DiOllLZSd2lR7IAKPKsUd6UY8uskOxI81hI116zNx17mIGFIIq\n"
operator|+
literal|"DdDgRbvzMNEgNlOxg/BD01kXOS4fhnT2F6ca3VGTgUtOdcdF3M9MtePWQLBzEDPz\n"
operator|+
literal|"8nx3O20HDupuQmG0IlRlc3R1c2VyIFRocmVlIDx0ZXN0M0BleGFtcGxlLmNvbT6J\n"
operator|+
literal|"AT4EEwECACgFAkLOp0UCGwMFCQHhM4AGCwkIBwMCBhUIAgkKCwQWAgMBAh4BAheA\n"
operator|+
literal|"AAoJEDzseBsX3hrNYg0H/2CMm5/JDQNSuRFCECWLrcOeimuvwbmkonNzOkvKbGXl\n"
operator|+
literal|"73GStISAksRWAHBQED1rEPC0NkFCDeVZO7dfSYLlsqKwV6uSh05Ra0F5XeniC12Y\n"
operator|+
literal|"pAyzoQyCGRS2wLaS822j0zUPXA8XLaO2blCuR+8sNu/oecMRcFK4S9NaApi3vdqB\n"
operator|+
literal|"NhLiN1/Lpqn1LfB8uIO+eaUf4PmCWbaPgzSkqcPfKZmocNXdgLV5Q80n3hc2y2nr\n"
operator|+
literal|"l+vDW2M+eVZuDHAok2BOD9uGKFfLAbaXLbX5btBW2L0UHtoEyiqkRfD6lX2laSLQ\n"
operator|+
literal|"mA6+eup7e4GS+s0vXBuVh8XEYddV6Yjt8H7/2thO41KdA5gEQs6nRQEIAM/833UH\n"
operator|+
literal|"K1DuFlOm7/n18dRMvs7BkXvg+hPquKWMG3beeE4sh1NG5DbRCdo6iacZLarWr3FD\n"
operator|+
literal|"z7J9+wswRhtHCh3pGHEuaJk52vRjQxlkNh5Fp5u2R4WF546bWqX45xPdLfHVTPyW\n"
operator|+
literal|"B9q7aVxE+6Q+MHa6lMoyTVnTVCOy3nshiihwdxLsxaga+QmaL0bAR+dRcO6ucj7T\n"
operator|+
literal|"DQXz1AJAVp26c0LXV9iErhFuuybUZKT0a9AjFoumMZ6l+k30sSdjSjpBMsNvPos0\n"
operator|+
literal|"dTPPRXUMu77o5sj+pHa4o8WctgB3o7BHQELpKgujZ2sKC9Nm395u6Q4cqUWihzb/\n"
operator|+
literal|"Y7rIRuNHJarI7vUAEQEAAQAH+gNBKDf7FDzwdM37Sz8Ej7OsPcIbekzPcOpV3mzM\n"
operator|+
literal|"u/NIuOY0QSvW7KRE8hwFlXjVZocJU/Z4Qqw+12pN55LusiRUrOq8eKuJIbl4QikI\n"
operator|+
literal|"Dea8XUqM+CKJPV3YZXs6YVdIuzrRBSLgsB/Glff5JlzkEjsRYVmmnto8edETL/MK\n"
operator|+
literal|"S9ClJqQiFKE4b01+Eh9oB/DfxzsiEf/a+rdRnWRh/jtpEwgeXcfmjhf+0zrzChu2\n"
operator|+
literal|"ylQQ5QOuwQNKJP6DvRu/W5pOaKH9tPDR31SccDJDdnDUzBD7oSsXl06DcfMNEa8q\n"
operator|+
literal|"PaNHLDDRNnqTEhwYSJ4r2emDFMxg7Kky+aatUNjAYk9vkgMEANnvumgr6/KCLWKc\n"
operator|+
literal|"D3fZE09N7BveGBBDQBYNGPFtx60WbKrSY3e2RSfgWbyEXkzwm1VlB2869T1we0rL\n"
operator|+
literal|"z6eV/TK5rrJQxJFHZ/anMxbQY0sCiOgqi6PKT03RTpA2N803hTym+oypy+5T6BFM\n"
operator|+
literal|"rtjXvwIZN/BgAE2JjA70crTAd1mvBAD0UFNAU9oE7K7sgDbni4EhxmDyaviBHfxV\n"
operator|+
literal|"PJP1ICUXAcEzAsz2T/L5TqZUD+LfYIkbf8wk2/mPZFfrCrQgCrzWn7KV1SHXkhf4\n"
operator|+
literal|"4Sg6Y6p0g0Jl3mWRPiQ6ALlOVQIkp5V8z4b0hTF2c4oct1Pzaeq+ZkahyvrhW06P\n"
operator|+
literal|"iaucRZb+mwP/aVTpkd4n/FyKCcbf9/KniYJ+Ou1OunsBQr/jzN+r0PKCb8l/ksig\n"
operator|+
literal|"i/M0NGetemq9CxYsJDAyJs1aO4SWgx5LbfcMmyXDuJ3sL0ztFLOES31Mih3ZJebg\n"
operator|+
literal|"xPpj2bB/67i2zeYRcjxQ116y23gOa2TWM8EE4TW7F/mQjw4fIPJ93ClBMIkBJQQY\n"
operator|+
literal|"AQIADwUCQs6nRQIbDAUJAeEzgAAKCRA87HgbF94azRiBB/4vAyOOjUjK3lDWjHGs\n"
operator|+
literal|"7mvEWJI/1MeLlGPswCSInJBa+HMiMI4tzq+hu5ejGThojNbmnL96GdzfDkMlP4Fe\n"
operator|+
literal|"yxb2rjtbNrD/R5tlXHmjX/QLzep4LCeMziP80fu8qUeiOej/Ecdny0w365PlMdt1\n"
operator|+
literal|"0RaYR8VEZX/DAie6JfElnfQcG5q8TIOH3i71qxV+kIoPqKWfQ0MXrNEJ3BYFfDGd\n"
operator|+
literal|"Ut8U1Kq9OuIHVRgGS7mMSyjgNqqp7MBeMY+PFFZaZel5yoYVjb9d3L8XvVv2eoa/\n"
operator|+
literal|"jPj5FUEUkE9uxNmwaD1PiV8DvBTYI+eQL4qzfu+3NTG2SfgQYtj5oiGHw8aL3U6Q\n"
operator|+
literal|"HDJb\n"
operator|+
literal|"=RrXv\n"
operator|+
literal|"-----END PGP PRIVATE KEY BLOCK-----\n"
argument_list|)
return|;
block|}
comment|/**    * A self-revoked key with no expiration.    *    *<pre>    * pub   2048R/7CA87821 2015-07-08 [revoked: 2015-07-08]    *       Key fingerprint = E328 CAB1 1F7E B1BC 1451  ABA5 0855 2A17 7CA8 7821    * uid                  Testuser Four&lt;test4@example.com&gt;    *</pre>    */
DECL|method|key4 ()
specifier|static
specifier|final
name|TestKey
name|key4
parameter_list|()
throws|throws
name|PGPException
throws|,
name|IOException
block|{
return|return
operator|new
name|TestKey
argument_list|(
literal|"-----BEGIN PGP PUBLIC KEY BLOCK-----\n"
operator|+
literal|"Version: GnuPG v1\n"
operator|+
literal|"\n"
operator|+
literal|"mQENBFWdTZwBCAC1jukp5mlitfq2sAmdtx1s1VbWh+buDbBY2kWcxbbssczozFUP\n"
operator|+
literal|"Ii67wPwjRbn3GM5+jY3GMsqKIrdyDlxeTxGWoU/qa2YkCQzgFGD/XJBqkVpP6osm\n"
operator|+
literal|"qFYSP0xST1iBkatkMHq5KMjrX2q2bGVLlchLF9eHrWSefMcfff1Vs/Y8F2RCo38y\n"
operator|+
literal|"gH88mbcvgyC+zq6Q2T3h5RiLK2IaZDNsn3uUoIMYHxI6oYtXXMSXRJlLJvamXVrB\n"
operator|+
literal|"7QAq8L8cNikJjZMz+bHtLtGDyVVp9tqo4yvMrHe6BYmBUte3tPYQlDVdEo7UqepR\n"
operator|+
literal|"uT7JbBOGBoD+9ngDrDggPUBAoa0h3VNOTyoDABEBAAGJAR8EIAECAAkFAlWdVXkC\n"
operator|+
literal|"HQIACgkQCFUqF3yoeCH4lgf/aBdTYqnwL1lreHbQaUXI0/B2zlMuoptoi/x+xjIB\n"
operator|+
literal|"7RszzaN3w0n4/87kUN2koNtgNymv2ccKTR1PiX+obscJhsWzNbz3/Cjtr/IpEQRd\n"
operator|+
literal|"E6qRptHDk0U2cHW4BYDSltndOktICdhWCWYLDxJHGjdyXqqqdEEFJ24u2fUJ3yF3\n"
operator|+
literal|"NF2Bxa6llrmLb2fVeVYBzQSztQopKRWP9nt3ySoeJQqRWjNBN2j7cC93nrLHZTvB\n"
operator|+
literal|"L/sWuTq5ecbXeeNVzxoBd21jmGrIUPNwGdDKdbTB0CjpLpVHOTwGByeRKQXhMlQB\n"
operator|+
literal|"pK96wUpxxtShtOjNjN1s9GEyLHwDiHSuHNYs/AxxFzf9nbQhVGVzdHVzZXIgRm91\n"
operator|+
literal|"ciA8dGVzdDRAZXhhbXBsZS5jb20+iQE4BBMBAgAiBQJVnU2cAhsDBgsJCAcDAgYV\n"
operator|+
literal|"CAIJCgsEFgIDAQIeAQIXgAAKCRAIVSoXfKh4IXsHCACSm9RIdxxqibAaxh+nm6w5\n"
operator|+
literal|"F5a6Hju5cdmkk9albDoQYh2eM8E5NdDq+r0qSSe2+ujDaQ4C95DZNJQESvIcHHHb\n"
operator|+
literal|"9AECrBfS8Yk86rX8hxVeYQczMkB9LdBHximTSoOr8L/eAxBE/VXDwust6EAe6Q1A\n"
operator|+
literal|"a3tlTTvCfcmw4PipvtP7F6UzFaq+QU6fvARpBATOcvVc2JU4JQOrxuNEQ2PKrSti\n"
operator|+
literal|"75S5mnVWm0pRebM+EorWBtlA0eOAeLNqCp87UwLdvUyOTRZT4DJ51eTxfrFADXrI\n"
operator|+
literal|"9/ejs3/YxCPYxaPicAlcldduuajU/s+9ifrUn0Npg2ILl8mQkNzqeerlBeecUV4E\n"
operator|+
literal|"uQENBFWdTZwBCADEOsK+mFQ/2uds9znkmAqrk24waVBpyPGrTTXtXX0dKhtQAsh6\n"
operator|+
literal|"QkZGkjLTnKxEsa9syqVckw+1JtCh44SP1gjqDUoShpBz5wIuksZ7q96Hx+F0TVG/\n"
operator|+
literal|"njS6GrWvwKhL2Lb9hYfdlrZiYtOOi0iiOzud25H/Ms15kC8tuQm7NWtANJJF4Sxo\n"
operator|+
literal|"Bxor6L/F4zunEkTL0L9/dp4qVrw23fJVKE38cSdxjB0u1qSDzLV/u0QJqlYxJAiE\n"
operator|+
literal|"ciwQN2uVnTY1/XSpouMy6LvbYU7B2uU/WohNmH3RiN/fQ6jJm4x+fCZ8+zqXMiZn\n"
operator|+
literal|"G2fPkwmxxK9cl64YnNGcTwsVt6BMbCHk9jHxABEBAAGJAR8EGAECAAkFAlWdTZwC\n"
operator|+
literal|"GwwACgkQCFUqF3yoeCGOdwf/TmoxH3pFBm/MDhY5Ct5FO0KvsgQk2ZgDa68HyQ8j\n"
operator|+
literal|"QYi1FUCtyDjsxf5KTfyvzpzcTpS7cyOwcJNtTj6UixwATkcivvYWYoOXghAsTo4f\n"
operator|+
literal|"1+j/x6ECq1+nYE6NpcAN7VRJpYMk2UO2qlhHCesTPGzsHchL7mwiYdhGrdiWGTpd\n"
operator|+
literal|"KI9WfOYDZZ9ZSw/QINJUyTRxrDnauOvVbhbAXc7jdKCkRQRZpsNlF//1Stg6nstj\n"
operator|+
literal|"FJ7SrjVdsMJNlihT6fG5ujmrty1/6b1VCLkIQfW5cWvzRzTBFytq7i4PVKh3u7Oz\n"
operator|+
literal|"tt9lf8s50zt2uBE/AKMkyE6IJLsBWpJPk7iFKkHGDx044Q==\n"
operator|+
literal|"=477N\n"
operator|+
literal|"-----END PGP PUBLIC KEY BLOCK-----\n"
argument_list|,
literal|"-----BEGIN PGP PRIVATE KEY BLOCK-----\n"
operator|+
literal|"Version: GnuPG v1\n"
operator|+
literal|"\n"
operator|+
literal|"lQOYBFWdTZwBCAC1jukp5mlitfq2sAmdtx1s1VbWh+buDbBY2kWcxbbssczozFUP\n"
operator|+
literal|"Ii67wPwjRbn3GM5+jY3GMsqKIrdyDlxeTxGWoU/qa2YkCQzgFGD/XJBqkVpP6osm\n"
operator|+
literal|"qFYSP0xST1iBkatkMHq5KMjrX2q2bGVLlchLF9eHrWSefMcfff1Vs/Y8F2RCo38y\n"
operator|+
literal|"gH88mbcvgyC+zq6Q2T3h5RiLK2IaZDNsn3uUoIMYHxI6oYtXXMSXRJlLJvamXVrB\n"
operator|+
literal|"7QAq8L8cNikJjZMz+bHtLtGDyVVp9tqo4yvMrHe6BYmBUte3tPYQlDVdEo7UqepR\n"
operator|+
literal|"uT7JbBOGBoD+9ngDrDggPUBAoa0h3VNOTyoDABEBAAEAB/4jqeZoOiACaV/Nygeh\n"
operator|+
literal|"iOpJSiDsNDbrFRpKYdnhwT69APIQ2q5sshi+/dopbZVpkeBiIJk0UR7TAp3JVEPV\n"
operator|+
literal|"rK92SMqjcCRYuMRkMeyZzMt7e4DjiN17ov6BSBjMZFSs4vnpTNKWk4ngHlaebe15\n"
operator|+
literal|"6vq0sYK/XpKQxU7yAzQjxR190P/F+QEL98zVG/9uqM8PupfdSm4Smp2cIpfta+JD\n"
operator|+
literal|"mO23HC6jAEm2RFwklovzgK3rbIjyiMuowIkAKx5xxRvpxMHf1l566b9zJrRi0xau\n"
operator|+
literal|"vp4J/lnBJtTMzCbsaaFxhrj23xvTXaWR+UkaGPCv7wheXQ9K7NAHwmH8YrR+cZx7\n"
operator|+
literal|"KbDlBADUTHZ+OhNslx/rkjRWrFuK9p49x7qxQc26kcqlGPbW6KOAMdUpwneQbhCG\n"
operator|+
literal|"a36E/GAZgsgQ4SUqn37EVCtd2Y9Dp0inPAujcZXSwgDHev6ea7fzbxT9KLtEgvQN\n"
operator|+
literal|"0vrFJDCPIt0wzGqNDw4wgFjF2rAafBO//Wu5K5QLW4hfzSguRQQA2u6DpVja/FYY\n"
operator|+
literal|"UHVh2HLiB8th4T+qogOsBe5mKEsGRPXtAh7QzJu36C4PJyHeNlmlMx+15cCFnovj\n"
operator|+
literal|"6cLpGn6ZP4okLyq2+VsW7wh/Vir+UZHoAO/cZRlOc1PsaQconcxxq30SsbaRQrAd\n"
operator|+
literal|"YargKlXU7HMFiK34nkidBV6vVW0+P6cD/jYRInM983KXqX5bYvqsM1Zyvvlu6otD\n"
operator|+
literal|"nG0F/nQYT7oaKKR46quDa+xHMxK8/Vu1+TabzY8XapnoYFaFvrl/d2rUBEZSoury\n"
operator|+
literal|"z2yfTyeomft9MGGQsCGAJ95bVDT+jBoohnYwfwdC7HG3qk0aK/TxFyUqvMOX7SFe\n"
operator|+
literal|"YT55n3HlD9InST+0IVRlc3R1c2VyIEZvdXIgPHRlc3Q0QGV4YW1wbGUuY29tPokB\n"
operator|+
literal|"OAQTAQIAIgUCVZ1NnAIbAwYLCQgHAwIGFQgCCQoLBBYCAwECHgECF4AACgkQCFUq\n"
operator|+
literal|"F3yoeCF7BwgAkpvUSHccaomwGsYfp5usOReWuh47uXHZpJPWpWw6EGIdnjPBOTXQ\n"
operator|+
literal|"6vq9Kkkntvrow2kOAveQ2TSUBEryHBxx2/QBAqwX0vGJPOq1/IcVXmEHMzJAfS3Q\n"
operator|+
literal|"R8Ypk0qDq/C/3gMQRP1Vw8LrLehAHukNQGt7ZU07wn3JsOD4qb7T+xelMxWqvkFO\n"
operator|+
literal|"n7wEaQQEznL1XNiVOCUDq8bjRENjyq0rYu+UuZp1VptKUXmzPhKK1gbZQNHjgHiz\n"
operator|+
literal|"agqfO1MC3b1Mjk0WU+AyedXk8X6xQA16yPf3o7N/2MQj2MWj4nAJXJXXbrmo1P7P\n"
operator|+
literal|"vYn61J9DaYNiC5fJkJDc6nnq5QXnnFFeBJ0DmARVnU2cAQgAxDrCvphUP9rnbPc5\n"
operator|+
literal|"5JgKq5NuMGlQacjxq0017V19HSobUALIekJGRpIy05ysRLGvbMqlXJMPtSbQoeOE\n"
operator|+
literal|"j9YI6g1KEoaQc+cCLpLGe6veh8fhdE1Rv540uhq1r8CoS9i2/YWH3Za2YmLTjotI\n"
operator|+
literal|"ojs7nduR/zLNeZAvLbkJuzVrQDSSReEsaAcaK+i/xeM7pxJEy9C/f3aeKla8Nt3y\n"
operator|+
literal|"VShN/HEncYwdLtakg8y1f7tECapWMSQIhHIsEDdrlZ02Nf10qaLjMui722FOwdrl\n"
operator|+
literal|"P1qITZh90Yjf30OoyZuMfnwmfPs6lzImZxtnz5MJscSvXJeuGJzRnE8LFbegTGwh\n"
operator|+
literal|"5PYx8QARAQABAAf8CeTumd6jbN7USXXDyQdzjkguR6mfwN29dcY8YF4U52oOm3+w\n"
operator|+
literal|"bR23XmqTvoDJXONatZEYOm093wP4hBktP3Vq2KZX5Ew9r2JoBUIoWOcHHvCQqSUW\n"
operator|+
literal|"6KMJBJNBMv3zXnOscmcPvTgStS5HfYn/XRLAhEqkd2ov2x/OiS8p0vM0F7YYSOdu\n"
operator|+
literal|"X6/nHeBCM5QSJl00kgcaeQYdIGL0bPv9DnoeAC2/yITEvtvs+MHZ7FjH8A45QjWn\n"
operator|+
literal|"DwfVoLg7WOc3wJtqJ55/r/2pylrWz0YYM8s6I3gbDilCF+Wb8tEIOaWJEwY73J1/\n"
operator|+
literal|"KQG5qlO3/hBlO80DtzNmi3ylRUuzGhTxQfvemwQA3EuZ+E48LJ3dwtdJhh5mFlWI\n"
operator|+
literal|"Ket21e5v1mqMxuLhf5/2CYcifM08u3EsEUdIr7egF25Sea8otqmCYcG8FuB37VY/\n"
operator|+
literal|"Hd4G/+YVVaaAB8EU6u64YfSswhzr0R2qWVLtkJr0EAephzdPdoUEtKDSdTxnXiDV\n"
operator|+
literal|"3vSqLWtZekScLa979uMEAOQIodJwxSvveKQWILjK67ZJr56X8YQZWA6rFsr1xMY0\n"
operator|+
literal|"N0GH+5k0k+tr4wT3H9uk9ZM1Z11G3c01mhzCNg5roFoKtftKUZRKxmbfjjDmAofl\n"
operator|+
literal|"bA6EZ0WHLdOwDLLTuXK09IsjjSHq0YHOxIlgFzIreuoxtz27bEEGhVFQg7xb0Lgb\n"
operator|+
literal|"A/9LP8i32L7/CHsuN0q4YjhJkkaB6JWUQMFqWwoAXALG3rnw/CGRYHmHpiAuSeHR\n"
operator|+
literal|"dSlZzndVi5poNC/d27msTx7ZuWlN7nOyywHBCTWV/nstm2I9rDhrHK7Axgq0Vv0y\n"
operator|+
literal|"bWAurUmEgDJHU3ZpsNVt4e30FooXIDLR4cnpRM7tILv39D4giQEfBBgBAgAJBQJV\n"
operator|+
literal|"nU2cAhsMAAoJEAhVKhd8qHghjncH/05qMR96RQZvzA4WOQreRTtCr7IEJNmYA2uv\n"
operator|+
literal|"B8kPI0GItRVArcg47MX+Sk38r86c3E6Uu3MjsHCTbU4+lIscAE5HIr72FmKDl4IQ\n"
operator|+
literal|"LE6OH9fo/8ehAqtfp2BOjaXADe1USaWDJNlDtqpYRwnrEzxs7B3IS+5sImHYRq3Y\n"
operator|+
literal|"lhk6XSiPVnzmA2WfWUsP0CDSVMk0caw52rjr1W4WwF3O43SgpEUEWabDZRf/9UrY\n"
operator|+
literal|"Op7LYxSe0q41XbDCTZYoU+nxubo5q7ctf+m9VQi5CEH1uXFr80c0wRcrau4uD1So\n"
operator|+
literal|"d7uzs7bfZX/LOdM7drgRPwCjJMhOiCS7AVqST5O4hSpBxg8dOOE=\n"
operator|+
literal|"=5aNq\n"
operator|+
literal|"-----END PGP PRIVATE KEY BLOCK-----\n"
argument_list|)
return|;
block|}
comment|// TODO(dborowitz): Figure out how to get gpg to revoke a key for someone
comment|// else.
DECL|field|pubArmored
specifier|private
specifier|final
name|String
name|pubArmored
decl_stmt|;
DECL|field|secArmored
specifier|private
specifier|final
name|String
name|secArmored
decl_stmt|;
DECL|field|pubRing
specifier|private
specifier|final
name|PGPPublicKeyRing
name|pubRing
decl_stmt|;
DECL|field|secRing
specifier|private
specifier|final
name|PGPSecretKeyRing
name|secRing
decl_stmt|;
DECL|method|TestKey (String pubArmored, String secArmored)
specifier|private
name|TestKey
parameter_list|(
name|String
name|pubArmored
parameter_list|,
name|String
name|secArmored
parameter_list|)
throws|throws
name|PGPException
throws|,
name|IOException
block|{
name|this
operator|.
name|pubArmored
operator|=
name|pubArmored
expr_stmt|;
name|this
operator|.
name|secArmored
operator|=
name|secArmored
expr_stmt|;
name|BcKeyFingerprintCalculator
name|fc
init|=
operator|new
name|BcKeyFingerprintCalculator
argument_list|()
decl_stmt|;
name|this
operator|.
name|pubRing
operator|=
operator|new
name|PGPPublicKeyRing
argument_list|(
name|newStream
argument_list|(
name|pubArmored
argument_list|)
argument_list|,
name|fc
argument_list|)
expr_stmt|;
name|this
operator|.
name|secRing
operator|=
operator|new
name|PGPSecretKeyRing
argument_list|(
name|newStream
argument_list|(
name|secArmored
argument_list|)
argument_list|,
name|fc
argument_list|)
expr_stmt|;
block|}
DECL|method|getPublicKeyArmored ()
name|String
name|getPublicKeyArmored
parameter_list|()
block|{
return|return
name|pubArmored
return|;
block|}
DECL|method|getSecretKeyArmored ()
name|String
name|getSecretKeyArmored
parameter_list|()
block|{
return|return
name|secArmored
return|;
block|}
DECL|method|getPublicKeyRing ()
name|PGPPublicKeyRing
name|getPublicKeyRing
parameter_list|()
block|{
return|return
name|pubRing
return|;
block|}
DECL|method|getPublicKey ()
name|PGPPublicKey
name|getPublicKey
parameter_list|()
block|{
return|return
name|pubRing
operator|.
name|getPublicKey
argument_list|()
return|;
block|}
DECL|method|getSecretKey ()
name|PGPSecretKey
name|getSecretKey
parameter_list|()
block|{
return|return
name|secRing
operator|.
name|getSecretKey
argument_list|()
return|;
block|}
DECL|method|getKeyId ()
name|long
name|getKeyId
parameter_list|()
block|{
return|return
name|getPublicKey
argument_list|()
operator|.
name|getKeyID
argument_list|()
return|;
block|}
DECL|method|getFirstUserId ()
name|String
name|getFirstUserId
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|getPublicKey
argument_list|()
operator|.
name|getUserIDs
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
DECL|method|getPrivateKey ()
name|PGPPrivateKey
name|getPrivateKey
parameter_list|()
throws|throws
name|PGPException
block|{
return|return
name|getSecretKey
argument_list|()
operator|.
name|extractPrivateKey
argument_list|(
operator|new
name|BcPBESecretKeyDecryptorBuilder
argument_list|(
operator|new
name|BcPGPDigestCalculatorProvider
argument_list|()
argument_list|)
comment|// All test keys have no passphrase.
operator|.
name|build
argument_list|(
operator|new
name|char
index|[
literal|0
index|]
argument_list|)
argument_list|)
return|;
block|}
DECL|method|newStream (String armored)
specifier|private
specifier|static
name|ArmoredInputStream
name|newStream
parameter_list|(
name|String
name|armored
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|ArmoredInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|Constants
operator|.
name|encode
argument_list|(
name|armored
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

