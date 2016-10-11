# androiddialog

Uso:

    AndroidDialog.show(Activity.this, AndroidDialog.Type.ERROR, "Title", "Message", handler);

Onde handler:

    final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                if (msg != null) {
        
                    if (msg.what == AndroidDialog.Result.OK.ordinal()) {
                        Toast.makeText(Activity.this, "BOTÃO OK", Toast.LENGTH_SHORT).show();
                    }
                    else if (msg.what == AndroidDialog.Result.YES.ordinal()) {
                        Toast.makeText(Activity.this, "BOTÃO YES", Toast.LENGTH_SHORT).show();
                    }
                    else if (msg.what == AndroidDialog.Result.NO.ordinal()) {
                        Toast.makeText(Activity.this, "BOTÃO NO", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    };
