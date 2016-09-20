ANTLR_VERS = '3.5.2'

maven_jar(
  name = 'java_runtime',
  artifact = 'org.antlr:antlr-runtime:' + ANTLR_VERS,
  sha1 = 'cd9cd41361c155f3af0f653009dcecb08d8b4afd',
)

maven_jar(
  name = 'stringtemplate',
  artifact = 'org.antlr:stringtemplate:4.0.2',
  sha1 = 'e28e09e2d44d60506a7bcb004d6c23ff35c6ac08',
)

maven_jar(
  name = 'org_antlr',
  artifact = 'org.antlr:antlr:' + ANTLR_VERS,
  sha1 = 'c4a65c950bfc3e7d04309c515b2177c00baf7764',
)

maven_jar(
  name = 'antlr27',
  artifact = 'antlr:antlr:2.7.7',
  sha1 = '83cd2cd674a217ade95a4bb83a8a14f351f48bd0',
)

GUICE_VERS = '4.1.0'

maven_jar(
  name = 'guice_library',
  artifact = 'com.google.inject:guice:' + GUICE_VERS,
  sha1 = 'eeb69005da379a10071aa4948c48d89250febb07',
)

maven_jar(
  name = 'guice_assistedinject',
  artifact = 'com.google.inject.extensions:guice-assistedinject:' + GUICE_VERS,
  sha1 = 'af799dd7e23e6fe8c988da12314582072b07edcb',
)

maven_jar(
  name = 'guice_servlet',
  artifact = 'com.google.inject.extensions:guice-servlet:' + GUICE_VERS,
  sha1 = '90ac2db772d9b85e2b05417b74f7464bcc061dcb',
)

maven_jar(
  name = 'multibindings',
  artifact = 'com.google.inject.extensions:guice-multibindings:' + GUICE_VERS,
  sha1 = '3b27257997ac51b0f8d19676f1ea170427e86d51',
)

maven_jar(
  name = 'aopalliance',
  artifact = 'aopalliance:aopalliance:1.0',
  sha1 = '0235ba8b489512805ac13a8f9ea77a1ca5ebe3e8',
)

maven_jar(
  name = 'javax_inject',
  artifact = 'javax.inject:javax.inject:1',
  sha1 = '6975da39a7040257bd51d21a231b76c915872d38',
)

maven_jar(
  name = 'servlet_api_3_1',
  artifact = 'org.apache.tomcat:tomcat-servlet-api:8.0.24',
  sha1 = '5d9e2e895e3111622720157d0aa540066d5fce3a',
)

GWT_VERS = '2.8.0-rc2'

maven_jar(
  name = 'user',
  artifact = 'com.google.gwt:gwt-user:' + GWT_VERS,
  sha1 = 'ad99b09a626c20cce2bdacf3726a51b2cd16b99e',
)

maven_jar(
  name = 'dev',
  artifact = 'com.google.gwt:gwt-dev:' + GWT_VERS,
  sha1 = 'd70a6feb4661c07488090cb81303415e9110b15a',
)

maven_jar(
  name = 'javax_validation',
  artifact = 'javax.validation:validation-api:1.0.0.GA',
  sha1 = 'b6bd7f9d78f6fdaa3c37dae18a4bd298915f328e',
)

maven_jar(
  name = 'jsinterop_annotations',
  artifact = 'com.google.jsinterop:jsinterop-annotations:1.0.0',
  sha1 = '23c3a3c060ffe4817e67673cc8294e154b0a4a95',
)

maven_jar(
  name = 'ant',
  artifact = 'ant:ant:1.6.5',
  sha1 = '7d18faf23df1a5c3a43613952e0e8a182664564b',
)

maven_jar(
  name = 'colt',
  artifact = 'colt:colt:1.2.0',
  sha1 = '0abc984f3adc760684d49e0f11ddf167ba516d4f',
)

maven_jar(
  name = 'tapestry',
  artifact = 'tapestry:tapestry:4.0.2',
  sha1 = 'e855a807425d522e958cbce8697f21e9d679b1f7',
)

maven_jar(
  name = 'w3c_css_sac',
  artifact = 'org.w3c.css:sac:1.3',
  sha1 = 'cdb2dcb4e22b83d6b32b93095f644c3462739e82',
)

http_jar(
  name = "javax_validation_src",
  url = "http://repo1.maven.org/maven2/javax/validation/validation-api/1.0.0.GA/validation-api-1.0.0.GA-sources.jar",
  sha256 = 'a394d52a9b7fe2bb14f0718d2b3c8308ffe8f37e911956012398d55c9f9f9b54',
)

JGIT_VERS = '4.4.1.201607150455-r.118-g1096652'

maven_jar(
  name = 'jgit',
  repository = 'http://gerrit-maven.storage.googleapis.com/',
  artifact = 'org.eclipse.jgit:org.eclipse.jgit:' + JGIT_VERS,
  sha1 = 'cd142b9030910babd119702f1c4eeae13ee90018',
)

maven_jar(
  name = 'jgit_servlet',
  repository = 'http://gerrit-maven.storage.googleapis.com/',
  artifact = 'org.eclipse.jgit:org.eclipse.jgit.http.server:' + JGIT_VERS,
  sha1 = 'fa67bf925001cfc663bf98772f37d5c5c1abd756',
)

# TODO(davido): Remove this hack when maven_jar supports pulling sources
# https://github.com/bazelbuild/bazel/issues/308
http_file(
  name = 'jgit_src',
  sha256 = '1a0b2d637359b1b51eba4d094491ef39877a6fc192e2fc1da0422a9adf04f0b8',
  url = 'http://gerrit-maven.storage.googleapis.com/org/eclipse/jgit/org.eclipse.jgit/' +
      '%s/org.eclipse.jgit-%s-sources.jar' % (JGIT_VERS, JGIT_VERS),
)

maven_jar(
  name = 'ewah',
  artifact = 'com.googlecode.javaewah:JavaEWAH:0.7.9',
  sha1 = 'eceaf316a8faf0e794296ebe158ae110c7d72a5a',
)

maven_jar(
  name = 'jgit_archive',
  repository = 'http://gerrit-maven.storage.googleapis.com/',
  artifact = 'org.eclipse.jgit:org.eclipse.jgit.archive:' + JGIT_VERS,
  sha1 = '3f45cd199e40a7c68ee07a1743c06d1c3d07308a',
)

maven_jar(
  name = 'jgit_junit',
  repository = 'http://gerrit-maven.storage.googleapis.com/',
  artifact = 'org.eclipse.jgit:org.eclipse.jgit.junit:' + JGIT_VERS,
  sha1 = 'dc7edb9c3060655c7fb93ab9b9349e815bab266f',
)

maven_jar(
  name = 'gwtjsonrpc',
  artifact = 'com.google.gerrit:gwtjsonrpc:1.11',
  sha1 = '9587c6f88f1964f1aa61212a162c776c71d77456',
)

http_jar(
  name = 'gwtjsonrpc_src',
  sha256 = 'fc503488872c022073e244015fcb6806a64b65afe546bdac2db167a3875fb418',
  url = 'http://repo.maven.apache.org/maven2/com/google/gerrit/gwtjsonrpc/1.11/gwtjsonrpc-1.11-sources.jar',
)

maven_jar(
  name = 'gson',
  artifact = 'com.google.code.gson:gson:2.7',
  sha1 = '751f548c85fa49f330cecbb1875893f971b33c4e',
)

maven_jar(
  name = 'gwtorm_client',
  artifact = 'com.google.gerrit:gwtorm:1.16',
  sha1 = 'b91331724669f8136426f0f4846ddcec0f6eb22f',
)

http_jar(
  name = 'gwtorm_client_src',
  sha256 = 'd3e482c9ac1f828aa853debe6545c16503fbbde3bda94b18f652d9830b7f84b1',
  url = 'http://repo.maven.apache.org/maven2/com/google/gerrit/gwtorm/1.16/gwtorm-1.16-sources.jar',
)

maven_jar(
  name = 'protobuf',
  artifact = 'com.google.protobuf:protobuf-java:2.5.0',
  sha1 = 'a10732c76bfacdbd633a7eb0f7968b1059a65dfa',
)

maven_jar(
  name = 'joda_time',
  artifact = 'joda-time:joda-time:2.9.4',
  sha1 = '1c295b462f16702ebe720bbb08f62e1ba80da41b',
)

maven_jar(
  name = 'joda_convert',
  artifact = 'org.joda:joda-convert:1.8.1',
  sha1 = '675642ac208e0b741bc9118dcbcae44c271b992a',
)

maven_jar(
  name = 'guava',
  artifact = 'com.google.guava:guava:19.0',
  sha1 = '6ce200f6b23222af3d8abb6b6459e6c44f4bb0e9',
)

maven_jar(
  name = 'velocity',
  artifact = 'org.apache.velocity:velocity:1.7',
  sha1 = '2ceb567b8f3f21118ecdec129fe1271dbc09aa7a',
)

maven_jar(
  name = 'jsch',
  artifact = 'com.jcraft:jsch:0.1.53',
  sha1 = '658b682d5c817b27ae795637dfec047c63d29935',
)

maven_jar(
  name = 'juniversalchardet',
  artifact = 'com.googlecode.juniversalchardet:juniversalchardet:1.0.3',
  sha1 = 'cd49678784c46aa8789c060538e0154013bb421b',
)

SLF4J_VERS = '1.7.7'

maven_jar(
  name = 'log_api',
  artifact = 'org.slf4j:slf4j-api:' + SLF4J_VERS,
  sha1 = '2b8019b6249bb05d81d3a3094e468753e2b21311',
)

maven_jar(
  name = 'log_nop',
  artifact = 'org.slf4j:slf4j-nop:' + SLF4J_VERS,
  sha1 = '6cca9a3b999ff28b7a35ca762b3197cd7e4c2ad1',
)

maven_jar(
  name = 'impl_log4j',
  artifact = 'org.slf4j:slf4j-log4j12:' + SLF4J_VERS,
  sha1 = '58f588119ffd1702c77ccab6acb54bfb41bed8bd',
)

maven_jar(
  name = 'jcl_over_slf4j',
  artifact = 'org.slf4j:jcl-over-slf4j:' + SLF4J_VERS,
  sha1 = '56003dcd0a31deea6391b9e2ef2f2dc90b205a92',
)

maven_jar(
  name = 'log4j',
  artifact = 'log4j:log4j:1.2.17',
  sha1 = '5af35056b4d257e4b64b9e8069c0746e8b08629f',
)

maven_jar(
  name = 'jsonevent_layout',
  artifact = 'net.logstash.log4j:jsonevent-layout:1.7',
  sha1 = '507713504f0ddb75ba512f62763519c43cf46fde',
)

maven_jar(
  name = 'json_smart',
  artifact = 'net.minidev:json-smart:1.1.1',
  sha1 = '24a2f903d25e004de30ac602c5b47f2d4e420a59',
)

maven_jar(
  name = 'args4j',
  artifact = 'args4j:args4j:2.0.26',
  sha1 = '01ebb18ebb3b379a74207d5af4ea7c8338ebd78b',
)

maven_jar(
  name = 'commons_codec',
  artifact = 'commons-codec:commons-codec:1.4',
  sha1 = '4216af16d38465bbab0f3dff8efa14204f7a399a',
)

maven_jar(
  name = 'commons_collections',
  artifact = 'commons-collections:commons-collections:3.2.2',
  sha1 = '8ad72fe39fa8c91eaaf12aadb21e0c3661fe26d5',
)

maven_jar(
  name = 'commons_compress',
  artifact = 'org.apache.commons:commons-compress:1.7',
  sha1 = 'ab365c96ee9bc88adcc6fa40d185c8e15a31410d',
)

maven_jar(
  name = 'commons_lang',
  artifact = 'commons-lang:commons-lang:2.6',
  sha1 = '0ce1edb914c94ebc388f086c6827e8bdeec71ac2',
)

maven_jar(
  name = 'commons_dbcp',
  artifact = 'commons-dbcp:commons-dbcp:1.4',
  sha1 = '30be73c965cc990b153a100aaaaafcf239f82d39',
)

maven_jar(
  name = 'commons_pool',
  artifact = 'commons-pool:commons-pool:1.5.5',
  sha1 = '7d8ffbdc47aa0c5a8afe5dc2aaf512f369f1d19b',
)

maven_jar(
  name = 'commons_net',
  artifact = 'commons-net:commons-net:3.5',
  sha1 = '342fc284019f590e1308056990fdb24a08f06318',
)

maven_jar(
  name = 'commons_oro',
  artifact = 'oro:oro:2.0.8',
  sha1 = '5592374f834645c4ae250f4c9fbb314c9369d698',
)

maven_jar(
  name = 'commons_validator',
  artifact = 'commons-validator:commons-validator:1.5.1',
  sha1 = '86d05a46e8f064b300657f751b5a98c62807e2a0',
)

maven_jar(
  name = 'automaton',
  artifact = 'dk.brics.automaton:automaton:1.11-8',
  sha1 = '6ebfa65eb431ff4b715a23be7a750cbc4cc96d0f',
)

maven_jar(
  name = 'pegdown',
  artifact = 'org.pegdown:pegdown:1.4.2',
  sha1 = 'd96db502ed832df867ff5d918f05b51ba3879ea7',
)

maven_jar(
  name = 'grappa',
  artifact = 'com.github.parboiled1:grappa:1.0.4',
  sha1 = 'ad4b44b9c305dad7aa1e680d4b5c8eec9c4fd6f5',
)

maven_jar(
  name = 'jitescript',
  artifact = 'me.qmx.jitescript:jitescript:0.4.0',
  sha1 = '2e35862b0435c1b027a21f3d6eecbe50e6e08d54',
)

OW2_VERS = '5.1'

maven_jar(
  name = 'ow2_asm',
  artifact = 'org.ow2.asm:asm:' + OW2_VERS,
  sha1 = '5ef31c4fe953b1fd00b8a88fa1d6820e8785bb45',
)

maven_jar(
  name = 'ow2_asm_analysis',
  artifact = 'org.ow2.asm:asm-analysis:' + OW2_VERS,
  sha1 = '6d1bf8989fc7901f868bee3863c44f21aa63d110',
)

maven_jar(
  name = 'ow2_asm_commons',
  artifact = 'org.ow2.asm:asm-commons:' + OW2_VERS,
  sha1 = '25d8a575034dd9cfcb375a39b5334f0ba9c8474e',
)

maven_jar(
  name = 'ow2_asm_tree',
  artifact = 'org.ow2.asm:asm-tree:' + OW2_VERS,
  sha1 = '87b38c12a0ea645791ead9d3e74ae5268d1d6c34',
)

maven_jar(
  name = 'ow2_asm_util',
  artifact = 'org.ow2.asm:asm-util:' + OW2_VERS,
  sha1 = 'b60e33a6bd0d71831e0c249816d01e6c1dd90a47',
)

maven_jar(
  name = 'auto_value',
  artifact = 'com.google.auto.value:auto-value:1.3-rc1',
  sha1 = 'b764e0fb7e11353fbff493b22fd6e83bf091a179',
)

maven_jar(
  name = 'tukaani_xz',
  artifact = 'org.tukaani:xz:1.4',
  sha1 = '18a9a2ce6abf32ea1b5fd31dae5210ad93f4e5e3',
)

LUCENE_VERS = '5.5.3'

maven_jar(
  name = 'lucene_core',
  artifact = 'org.apache.lucene:lucene-core:' + LUCENE_VERS,
  sha1 = '20540c6347259f35a0d264605b22ce2a13917066',
)

maven_jar(
  name = 'lucene_analyzers_common',
  artifact = 'org.apache.lucene:lucene-analyzers-common:' + LUCENE_VERS,
  sha1 = 'cf734ab72813af33dc1544ce61abc5c17b9d35e9',
)

maven_jar(
  name = 'backward_codecs',
  artifact = 'org.apache.lucene:lucene-backward-codecs:' + LUCENE_VERS,
  sha1 = 'a167789e52a9dc6d93bf3b588f79fdc9d7559c15',
)

maven_jar(
  name = 'lucene_misc',
  artifact = 'org.apache.lucene:lucene-misc:' + LUCENE_VERS,
  sha1 = 'e356975c46447f06c71842632d0af9ec1baecfce',
)

maven_jar(
  name = 'lucene_queryparser',
  artifact = 'org.apache.lucene:lucene-queryparser:' + LUCENE_VERS,
  sha1 = 'e2452203d2c44cac5ac42b34e5dcc0a44bf29a53',
)

maven_jar(
  name = 'mime_util',
  artifact = 'eu.medsea.mimeutil:mime-util:2.1.3',
  sha1 = '0c9cfae15c74f62491d4f28def0dff1dabe52a47',
)

PROLOG_VERS = '1.4.1'

maven_jar(
  name = 'prolog_runtime',
  repository = 'http://gerrit-maven.storage.googleapis.com/',
  artifact = 'com.googlecode.prolog-cafe:prolog-runtime:' + PROLOG_VERS,
  sha1 = 'c5d9f92e49c485969dcd424dfc0c08125b5f8246',
)

maven_jar(
  name = 'prolog_compiler',
  repository = 'http://gerrit-maven.storage.googleapis.com/',
  artifact = 'com.googlecode.prolog-cafe:prolog-compiler:' + PROLOG_VERS,
  sha1 = 'ac24044c6ec166fdcb352b78b80d187ead3eff41',
)

maven_jar(
  name = 'prolog_io',
  repository = 'http://gerrit-maven.storage.googleapis.com/',
  artifact = 'com.googlecode.prolog-cafe:prolog-io:' + PROLOG_VERS,
  sha1 = 'b072426a4b1b8af5e914026d298ee0358a8bb5aa',
)

maven_jar(
  name = 'cafeteria',
  repository = 'http://gerrit-maven.storage.googleapis.com/',
  artifact = 'com.googlecode.prolog-cafe:prolog-cafeteria:' + PROLOG_VERS,
  sha1 = '8cbc3b0c19e7167c42d3f11667b21cb21ddec641',
)

maven_jar(
  name = 'guava_retrying',
  artifact = 'com.github.rholder:guava-retrying:2.0.0',
  sha1 = '974bc0a04a11cc4806f7c20a34703bd23c34e7f4',
)

maven_jar(
  name = 'jsr305',
  artifact = 'com.google.code.findbugs:jsr305:3.0.1',
  sha1 = 'f7be08ec23c21485b9b5a1cf1654c2ec8c58168d',
)

maven_jar(
  name = 'blame_cache',
  repository = 'http://gerrit-maven.storage.googleapis.com/',
  artifact = 'com/google/gitiles:blame-cache:0.1-9',
  sha1 = '51d35e6f8bbc2412265066cea9653dd758c95826',
)

# Keep this version of Soy synchronized with the version used in Gitiles.
maven_jar(
  name = 'soy',
  artifact = 'com.google.template:soy:2016-08-09',
  sha1 = '43d33651e95480d515fe26c10a662faafe3ad1e4',
)

maven_jar(
  name = 'icu4j',
  artifact = 'com.ibm.icu:icu4j:57.1',
  sha1 = '198ea005f41219f038f4291f0b0e9f3259730e92',
)

maven_jar(
  name = 'dropwizard_core',
  artifact = 'io.dropwizard.metrics:metrics-core:3.1.2',
  sha1 = '224f03afd2521c6c94632f566beb1bb5ee32cf07',
)

# This version must match the version that also appears in
# gerrit-pgm/src/main/resources/com/google/gerrit/pgm/init/libraries.config
BC_VERS = '1.52'

maven_jar(
  name = 'bcprov',
  artifact = 'org.bouncycastle:bcprov-jdk15on:' + BC_VERS,
  sha1 = '88a941faf9819d371e3174b5ed56a3f3f7d73269',
)

maven_jar(
  name = 'bcpg',
  artifact = 'org.bouncycastle:bcpg-jdk15on:' + BC_VERS,
  sha1 = 'ff4665a4b5633ff6894209d5dd10b7e612291858',
)

maven_jar(
  name = 'bcpkix',
  artifact = 'org.bouncycastle:bcpkix-jdk15on:' + BC_VERS,
  sha1 = 'b8ffac2bbc6626f86909589c8cc63637cc936504',
)

maven_jar(
  name = 'sshd',
  artifact = 'org.apache.sshd:sshd-core:1.2.0',
  sha1 = '4bc24a8228ba83dac832680366cf219da71dae8e',
)

maven_jar(
  name = 'mina_core',
  artifact = 'org.apache.mina:mina-core:2.0.10',
  sha1 = 'a1cb1136b104219d6238de886bf5a3ea4554eb58',
)

maven_jar(
  name = 'h2',
  artifact = 'com.h2database:h2:1.3.176',
  sha1 = 'fd369423346b2f1525c413e33f8cf95b09c92cbd',
)

HTTPCOMP_VERS = '4.4.1'

maven_jar(
  name = 'fluent_hc',
  artifact = 'org.apache.httpcomponents:fluent-hc:' + HTTPCOMP_VERS,
  sha1 = '96fb842b68a44cc640c661186828b60590c71261',
)

maven_jar(
  name = 'httpclient',
  artifact = 'org.apache.httpcomponents:httpclient:' + HTTPCOMP_VERS,
  sha1 = '016d0bc512222f1253ee6b64d389c84e22f697f0',
)

maven_jar(
  name = 'httpcore',
  artifact = 'org.apache.httpcomponents:httpcore:' + HTTPCOMP_VERS,
  sha1 = 'f5aa318bda4c6c8d688c9d00b90681dcd82ce636',
)

maven_jar(
  name = 'httpmime',
  artifact = 'org.apache.httpcomponents:httpmime:' + HTTPCOMP_VERS,
  sha1 = '2f8757f5ac5e38f46c794e5229d1f3c522e9b1df',
)

# Test-only dependencies below.

maven_jar(
  name = 'jimfs',
  artifact = 'com.google.jimfs:jimfs:1.1',
  sha1 = '8fbd0579dc68aba6186935cc1bee21d2f3e7ec1c',
)

maven_jar(
  name = 'junit',
  artifact = 'junit:junit:4.11',
  sha1 = '4e031bb61df09069aeb2bffb4019e7a5034a4ee0',
)

maven_jar(
  name = 'hamcrest_core',
  artifact = 'org.hamcrest:hamcrest-core:1.3',
  sha1 = '42a25dc3219429f0e5d060061f71acb49bf010a0',
)

maven_jar(
  name = 'truth',
  artifact = 'com.google.truth:truth:0.28',
  sha1 = '0a388c7877c845ff4b8e19689dda5ac9d34622c4',
)

maven_jar(
  name = 'easymock',
  artifact = 'org.easymock:easymock:3.4', # When bumping the version
  sha1 = '9fdeea183a399f25c2469497612cad131e920fa3',
)

maven_jar(
  name = 'cglib_2_2',
  artifact = 'cglib:cglib-nodep:2.2.2',
  sha1 = '00d456bb230c70c0b95c76fb28e429d42f275941',
)

maven_jar(
  name = 'objenesis',
  artifact = 'org.objenesis:objenesis:2.2',
  sha1 = '3fb533efdaa50a768c394aa4624144cf8df17845',
)

POWERM_VERS = '1.6.4'

maven_jar(
  name = 'powermock_module_junit4',
  artifact = 'org.powermock:powermock-module-junit4:' + POWERM_VERS,
  sha1 = '8692eb1d9bb8eb1310ffe8a20c2da7ee6d1b5994',
)

maven_jar(
  name = 'powermock_module_junit4_common',
  artifact = 'org.powermock:powermock-module-junit4-common:' + POWERM_VERS,
  sha1 = 'b0b578da443794ceb8224bd5f5f852aaf40f1b81',
)

maven_jar(
  name = 'powermock_reflect',
  artifact = 'org.powermock:powermock-reflect:' + POWERM_VERS,
  sha1 = '5532f4e7c42db4bca4778bc9f1afcd4b0ee0b893',
)

maven_jar(
  name = 'powermock_api_easymock',
  artifact = 'org.powermock:powermock-api-easymock:' + POWERM_VERS,
  sha1 = '5c385a0d8c13f84b731b75c6e90319c532f80b45',
)

maven_jar(
  name = 'powermock_api_support',
  artifact = 'org.powermock:powermock-api-support:' + POWERM_VERS,
  sha1 = '314daafb761541293595630e10a3699ebc07881d',
)

maven_jar(
  name = 'powermock_core',
  artifact = 'org.powermock:powermock-core:' + POWERM_VERS,
  sha1 = '85fb32e9ccba748d569fc36aef92e0b9e7f40b87',
)

maven_jar(
  name = 'javassist',
  artifact = 'org.javassist:javassist:3.20.0-GA',
  sha1 = 'a9cbcdfb7e9f86fbc74d3afae65f2248bfbf82a0',
)

maven_jar(
  name = 'derby',
  artifact = 'org.apache.derby:derby:10.11.1.1',
  sha1 = 'df4b50061e8e4c348ce243b921f53ee63ba9bbe1',
)

JETTY_VERS = '9.3.11.v20160721'

maven_jar(
  name = 'jetty_servlet',
  artifact = 'org.eclipse.jetty:jetty-servlet:' + JETTY_VERS,
  sha1 = 'd550147b85c73ea81084a4ac7915ba7f609021c5',
)

maven_jar(
  name = 'jetty_security',
  artifact = 'org.eclipse.jetty:jetty-security:' + JETTY_VERS,
  sha1 = '1cbefc5d1196b9e1ca6f4cc36738998a6ebde8bf',
)

maven_jar(
  name = 'jetty_servlets',
  artifact = 'org.eclipse.jetty:jetty-servlets:' + JETTY_VERS,
  sha1 = 'a9f7a43977151a463aa21a9b0e882aa3d25452ef',
)

maven_jar(
  name = 'jetty_server',
  artifact = 'org.eclipse.jetty:jetty-server:' + JETTY_VERS,
  sha1 = 'd932e0dc1e9bd4839ae446754615163d60271a66',
)

maven_jar(
  name = 'jetty_jmx',
  artifact = 'org.eclipse.jetty:jetty-jmx:' + JETTY_VERS,
  sha1 = '21a658d2f5eb87c23eef4911966625ea95f66d32',
)

maven_jar(
  name = 'jetty_continuation',
  artifact = 'org.eclipse.jetty:jetty-continuation:' + JETTY_VERS,
  sha1 = '92a91c0dcc5f5d779a1c9f94038332be3f46c9df',
)

maven_jar(
  name = 'jetty_http',
  artifact = 'org.eclipse.jetty:jetty-http:' + JETTY_VERS,
  sha1 = 'dcfb95e5b886a981bb76467b911c5b706117f9cf',
)

maven_jar(
  name = 'jetty_io',
  artifact = 'org.eclipse.jetty:jetty-io:' + JETTY_VERS,
  sha1 = 'db5f4f481159894a4b670072a34917b5414d0c98',
)

maven_jar(
  name = 'jetty_util',
  artifact = 'org.eclipse.jetty:jetty-util:' + JETTY_VERS,
  sha1 = '1812ffd5a04698051180d582c146ca807760c808',
)

maven_jar(
  name = 'openid_consumer',
  artifact = 'org.openid4java:openid4java:0.9.8',
  sha1 = 'de4f1b33d3b0f0b2ab1d32834ec1190b39db4160',
)

maven_jar(
  name = 'nekohtml',
  artifact = 'net.sourceforge.nekohtml:nekohtml:1.9.10',
  sha1 = '14052461031a7054aa094f5573792feb6686d3de',
)

maven_jar(
  name = 'xerces',
  artifact = 'xerces:xercesImpl:2.8.1',
  sha1 = '25101e37ec0c907db6f0612cbf106ee519c1aef1',
)

maven_jar(
  name = 'postgresql',
  artifact = 'postgresql:postgresql:9.1-901-1.jdbc4',
  sha1 = '9bfabe48876ec38f6cbaa6931bad05c64a9ea942',
)

CM_VERSION = '5.18.2'

maven_jar(
  name = 'codemirror_minified',
  artifact = 'org.webjars.npm:codemirror-minified:' + CM_VERSION,
  sha1 = '6755af157a7eaf2401468906bef67bbacc3c97f6',
)

maven_jar(
  name = 'codemirror_original',
  artifact = 'org.webjars.npm:codemirror:' + CM_VERSION,
  sha1 = '18c721ae88eed27cddb458c42f5d221fa3d9713e',
)

maven_jar(
  name = 'diff_match_patch',
  artifact = 'org.webjars:google-diff-match-patch:20121119-1',
  sha1 = '0cf1782dbcb8359d95070da9176059a5a9d37709',
)
