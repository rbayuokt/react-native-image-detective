
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNImageDetectiveSpec.h"

@interface ImageDetective : NSObject <NativeImageDetectiveSpec>
#else
#import <React/RCTBridgeModule.h>

@interface ImageDetective : NSObject <RCTBridgeModule>
#endif

@end
