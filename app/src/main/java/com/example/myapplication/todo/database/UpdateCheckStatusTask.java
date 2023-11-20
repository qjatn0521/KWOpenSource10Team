package com.example.myapplication.todo.database;

import android.os.AsyncTask;

public class UpdateCheckStatusTask extends AsyncTask<Long, Void, Void> {
    private TodoDBDao todoDao;
    private boolean updateToTrue; // true면 updateCheckStatus, false면 updateCheckStatusToFalse 호출

    public UpdateCheckStatusTask(TodoDBDao todoDao, boolean updateToTrue) {
        this.todoDao = todoDao;
        this.updateToTrue = updateToTrue;
    }

    @Override
    protected Void doInBackground(Long... todoIds) {
        if (todoIds != null && todoIds.length > 0) {
            long todoId = todoIds[0];
            if (updateToTrue) {
                todoDao.updateCheckStatus(todoId);
            } else {
                todoDao.updateCheckStatusToFalse(todoId);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        // 작업이 완료된 후에 호출되는 메서드
        // 예를 들어, UI 업데이트 등의 작업을 수행할 수 있습니다.
    }
}
