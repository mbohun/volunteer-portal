/**
 * Scott Sisters Lookup function for displaying a painting number
 *
 * Created by IntelliJ IDEA.
 * User: nick
 * Date: 28/10/11
 * Time: 12:36 PM
 * To change this template use File | Settings | File Templates.
 */

var sketchesMap = {
    3: "ams563_400a_medium.jpg",
    4: "ams563_310_medium.jpg",
    5: "ams563_310_medium.jpg",
    6: "ams563_142a_medium.jpg",
    10: "ams563_317_medium.jpg",
    11: "ams563_040a_medium.jpg",
    11: "ams563_317_medium.jpg",
    12: "ams563_158_medium.jpg",
    38: "ams563_044_medium.jpg",
    39: "ams563_044_medium.jpg",
    40: "ams563_044_medium.jpg",
    43: "ams563_285_medium.jpg",
    45: "ams563_162a_medium.jpg",
    55: "ams563_044_medium.jpg",
    56: "ams563_075_medium.jpg",
    57: "ams563_075_medium.jpg",
    58: "ams563_075_medium.jpg",
    59: "ams563_075_medium.jpg",
    66: "ams563_153_medium.jpg",
    69: "ams563_307_medium.jpg",
    70: "ams563_307_medium.jpg",
    71: "ams563_307_medium.jpg",
    72: "ams563_320a_medium.jpg",
    73: "ams563_320a_medium.jpg",
    74: "ams563_320a_medium.jpg",
    75: "ams563_305_medium.jpg",
    76: "ams563_305_medium.jpg",
    77: "ams563_305_medium.jpg",
    80: "ams563_279_medium.jpg",
    81: "ams563_279_medium.jpg",
    82: "ams563_279_medium.jpg",
    83: "ams563_045_medium.jpg",
    84: "ams563_045_medium.jpg",
    85: "ams563_045_medium.jpg",
    86 : "ams563_045_medium.jpg",
    89: "ams563_279_medium.jpg",
    90: "ams563_304a_medium.jpg",
    91: "ams563_304a_medium.jpg",
    92: "ams563_304a_medium.jpg",
    93: "ams563_304a_medium.jpg",
    94: "ams563_292_medium.jpg",
    95: "ams563_292_medium.jpg",
    96: "ams563_292_medium.jpg",
    102: "ams563_174_medium.jpg",
    103: "ams563_174_medium.jpg",
    110: "ams563_319_medium.jpg",
    111: "ams563_319_medium.jpg",
    112: "ams563_319_medium.jpg",
    113: "ams563_348_medium.jpg",
    114: "ams563_348_medium.jpg",
    115: "ams563_350_medium.jpg",
    116: "ams563_350_medium.jpg",
    117: "ams563_408_medium.jpg",
    118: "ams563_408_medium.jpg",
    119: "ams563_408_medium.jpg",
    120: "ams563_324_medium.jpg",
    121: "ams563_324_medium.jpg",
    122: "ams563_324_medium.jpg",
    123: "ams563_324_medium.jpg",
    138: "ams563_354_medium.jpg",
    139: "ams563_354_medium.jpg",
    140: "ams563_325a_medium.jpg",
    141: "ams563_325a_medium.jpg",
    142: "ams563_325a_medium.jpg",
    143: "ams563_325a_medium.jpg",
    144: "ams563_325a_medium.jpg",
    145: "ams563_325a_medium.jpg",
    152: "ams563_318_medium.jpg",
    153: "ams563_318_medium.jpg",
    154: "ams563_318_medium.jpg",
    187: "ams563_278_medium.jpg",
    188: "ams563_278_medium.jpg",
    189: "ams563_278_medium.jpg",
    190: "ams563_278_medium.jpg",
    191: "ams563_278_medium.jpg",
    192: "ams563_278_medium.jpg",
    193: "ams563_278_medium.jpg",
    212: "ams563_280_medium.jpg",
    213: "ams563_280_medium.jpg",
    253: "ams563_323a_medium.jpg",
    254: "ams563_323a_medium.jpg",
    255: "ams563_323a_medium.jpg",
    257: "ams563_066_medium.jpg",
    258: "ams563_066_medium.jpg",
    260: "ams563_066_medium.jpg",
    261: "ams563_066_medium.jpg",
    262: "ams563_066_medium.jpg",
    269: "ams563_196_medium.jpg",
    270: "ams563_196_medium.jpg",
    271: "ams563_196_medium.jpg",
    272: "ams563_196_medium.jpg",
    276: "ams563_321_medium.jpg",
    277: "ams563_321_medium.jpg",
    278: "ams563_321_medium.jpg",
    283: "ams563_054_medium.jpg",
    284: "ams563_054_medium.jpg",
    285: "ams563_054_medium.jpg",
    286: "ams563_178_medium.jpg",
    287: "ams563_178_medium.jpg",
    288: "ams563_178_medium.jpg",
    297: "ams563_274_medium.jpg",
    298: "ams563_274_medium.jpg",
    299: "ams563_274_medium.jpg",
    300: "ams563_274_medium.jpg",
    304: "ams563_342_medium.jpg",
    305: "ams563_342_medium.jpg",
    306: "ams563_342_medium.jpg",
    307: "ams563_342_medium.jpg",
    308: "ams563_055_medium.jpg",
    309: "ams563_055_medium.jpg",
    310: "ams563_055_medium.jpg",
    326: "ams563_077a_medium.jpg",
    328: "ams563_303a_medium.jpg",
    334: "ams563_202_medium.jpg",
    335: "ams563_182a_medium.jpg",
    340: "ams563_322a_medium.jpg",
    343: "ams563_291a_medium.jpg",
    348: "ams563_189a_medium.jpg",
    351: "ams563_002a_medium.jpg",
    351: "ams563_030_medium.jpg",
    356: "ams563_070a_medium.jpg",
    359: "ams563_057a_medium.jpg",
    368: "ams563_181a_medium.jpg",
    371: "ams563_069a_medium.jpg",
    374: "ams563_138a_medium.jpg",
    377: "ams563_353a_medium.jpg",
    379: "ams563_337a_medium.jpg",
    381: "ams563_025a_medium.jpg",
    385: "ams563_029a_medium.jpg",
    386: "ams563_083a_medium.jpg",
    388: "ams563_047a_medium.jpg",
    389: "ams563_205a_medium.jpg",
    391: "ams563_033a_medium.jpg",
    394: "ams563_183a_medium.jpg",
    398: "ams563_053a_medium.jpg",
    401: "ams563_082a_medium.jpg",
    407: "ams563_046a_medium.jpg",
    408: "ams563_037a_medium.jpg",
    410: "ams563_081a_medium.jpg",
    411: "ams563_067a_medium.jpg",
    412: "ams563_073_1a_medium.jpg",
    416: "ams563_343a_medium.jpg",
    417: "ams563_143a_medium.jpg",
    419: "ams563_180a_medium.jpg",
    422: "ams563_051a_medium.jpg",
    425: "ams563_050a_medium.jpg",
    427: "ams563_341a_medium.jpg",
    436: "ams563_332a_medium.jpg",
    437: "ams563_043a_medium.jpg",
    439: "ams563_039a_medium.jpg",
    442: "ams563_192a_medium.jpg",
    444: "ams563_195a_medium.jpg",
    446: "ams563_193a_medium.jpg",
    449: "ams563_188a_medium.jpg",
    451: "ams563_199a_medium.jpg",
    453: "ams563_175a_medium.jpg",
    454: "ams563_026a_medium.jpg",
    456: "ams563_035a_medium.jpg",
    457: "ams563_032a_medium.jpg",
    458: "ams563_004a_medium.jpg",
    460: "ams563_028a_medium.jpg",
    464: "ams563_049a_medium.jpg",
    467: "ams563_058a_medium.jpg",
    468: "ams563_190a_medium.jpg",
    469: "ams563_338a_medium.jpg",
    470: "ams563_048a_medium.jpg",
    471: "ams563_267a_medium.jpg",
    479: "ams563_204a_medium.jpg",
    482: "ams563_056a_medium.jpg",
    483: "ams563_184a_medium.jpg",
    484: "ams563_197a_medium.jpg",
    485: "ams563_042a_medium.jpg",
    486: "ams563_074a_medium.jpg",
    487: "ams563_071a_medium.jpg",
    488: "ams563_191a_medium.jpg",
    489: "ams563_052a_medium.jpg",
    493: "ams563_061a_medium.jpg",
    495: "ams563_312a_medium.jpg",
    497: "ams563_141a_medium.jpg",
    498: "ams563_313a_medium.jpg",
    502: "ams563_198a_medium.jpg",
    504: "ams563_326a_medium.jpg",
    505: "ams563_314a_medium.jpg",
    507: "ams563_315a_medium.jpg",
    508: "ams563_186a_medium.jpg",
    511: "ams563_041a_medium.jpg",
    512: "ams563_179a_medium.jpg",
    515: "ams563_027a_medium.jpg",
    516: "ams563_062a_medium.jpg",
    518: "ams563_344a_medium.jpg",
    522: "ams563_201a_medium.jpg",
    562: "ams563_076a_medium.jpg",
    602: "ams563_203a_medium.jpg"
};

var prefixUri = "http://volunteer.ala.org.au/uploads/scottSisters/Sketches/";

function getSketchUri(sketchNumber) {
    var sketchUri = null;
    var intRegex = /^\d+$/;

    if (intRegex.test(sketchNumber) && sketchesMap[sketchNumber]) {
        sketchUri = prefixUri + sketchesMap[sketchNumber]
    }

    return sketchUri;
}