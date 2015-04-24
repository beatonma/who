# who

Once set up, 'Who' can send an email to yourself containing the active account types and associated email addresses from AccountManager. If your device gets stolen, trigger the activity 'com.beatonma.who.Invisible' (using Tasker or something similar - you could set it to respond to a particular text message or whatever) to send the email in the background without alerting the thief!

WARNING: Please use a throwaway Gmail account for sending the info! The activity requires 'Access for less secure apps' to be enabled on the Google account, which in turn requires 2-factor authentication to be disabled. Basically, the account you use as the sender needs to be crippled, security wise, so don't use an account that actually matters.



Built after reading this thread: http://www.reddit.com/r/Android/comments/33l0yy/long_story_got_my_phone_back_after_losing_it_over/cqlvt7p

...and particularly this comment: http://www.reddit.com/r/Android/comments/33l0yy/long_story_got_my_phone_back_after_losing_it_over/cqlvt7p

'Access for less secure apps' settings page: https://www.google.com/settings/security/lesssecureapps


'Who' uses these excellent libaries:
  https://github.com/rengwuxian/MaterialEditText
  https://github.com/afollestad/material-dialogs
