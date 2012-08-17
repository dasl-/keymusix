#include <stdio.h>
#import <ApplicationServices/ApplicationServices.h>

FILE *fp;

CGEventRef callbackKeyEvent (
    CGEventTapProxy proxy, 
    CGEventType type, 
    CGEventRef event, 
    void *refcon) {
  fprintf(fp, "%d\n", (int) CGEventGetIntegerValueField(event, kCGKeyboardEventKeycode));
  return event;
}

int main() 
{
  fp = fopen("keypresses", "w");

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

  //CFRunLoopRun();
  int i = 0;
  for (i=0; i<=10; i++) {
    CFRunLoopRunInMode(kCFRunLoopDefaultMode, 5, false);
  }

  fclose(fp);

  return 0;
}


