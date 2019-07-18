/*
 * Copyright 2018-present MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mongodb.stitch.android.tutorials.todo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.google.android.gms.tasks.Task;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.json.JSONObject;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoItemViewHolder> {
  private static final String TAG = TodoAdapter.class.getSimpleName();
  private final ItemUpdater itemUpdater;
  private List<TodoItem> todoItems;


  TodoAdapter(final List<TodoItem> todoItems, final ItemUpdater itemUpdater) {
    this.todoItems = todoItems;
    this.itemUpdater = itemUpdater;
  }

  @NonNull
  @Override
  public TodoItemViewHolder onCreateViewHolder(
      @NonNull final ViewGroup parent,
      final int viewType
  ) {
    final View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.todo_item, parent, false);

    return new TodoItemViewHolder(v);
  }

  // Replace the contents of a view (invoked by the layout manager)
  @Override
  public void onBindViewHolder(@NonNull final TodoItemViewHolder holder, final int position) {
    final TodoItem item = todoItems.get(position);

    //holder.taskTextView.requestLayout();

    //View ll = LayoutInflater.from(holder.taskTextView.getContext()).inflate(R.layout.todo_item,null,false);
    //TableRow tableRow = holder.taskTextView;

    //TextView store_name  = (TextView) tableRow.findViewById(R.id.StoreName);
    //TextView store_location  = (TextView) tableRow.findViewById(R.id.StoreLocation);
    //TextView item_description  = (TextView) tableRow.findViewById(R.id.ItemDescription);
    //TextView item_quantity  = (TextView) tableRow.findViewById(R.id.ItemQuantity);
    //TextView org_price  = (TextView) tableRow.findViewById(R.id.OriginalPrice);
    //TextView sale_price  = (TextView) tableRow.findViewById(R.id.SalePrice);
    //TextView expiry_date  = (TextView) tableRow.findViewById(R.id.ExpiryDate);

    //TextView store_name  = (TextView) tableRow.findViewById(R.id.StoreName);
    //TextView store_location  = (TextView) tableRow.findViewById(R.id.StoreLocation);
    //TextView item_description = (TextView) tableRow.findViewById(R.id.ItemDescription);

    TableRow tableRow = new TableRow(holder.taskTextView.getContext());

    TableRow.LayoutParams rowParams = new TableRow.LayoutParams(
            TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.MATCH_PARENT);
    rowParams.gravity = Gravity.CENTER;

    //tableRow.setBackgroundColor(123456);
    TextView store_name  = new TextView(holder.taskTextView.getContext());
    TextView store_location  = new TextView(holder.taskTextView.getContext());
    TextView item_description = new TextView(holder.taskTextView.getContext());
    TextView item_quantity = new TextView(holder.taskTextView.getContext());
    TextView org_price  = new TextView(holder.taskTextView.getContext());
    TextView sale_price = new TextView(holder.taskTextView.getContext());
    TextView expiry_date = new TextView(holder.taskTextView.getContext());

    store_name.setText(item.getStore_name());
    store_location.setText(item.getStore_location());
    item_description.setText(item.getItem_description());
    item_quantity.setText("" + item.getItem_quantity());
    org_price.setText("" + item.getOriginal_price());
    sale_price.setText("" + item.getSale_price());
    expiry_date.setText(item.getExpiry_date());


    tableRow.addView(store_name);
    tableRow.addView(store_location);
    tableRow.addView(item_description);
    tableRow.addView(item_quantity);
    tableRow.addView(org_price);
    tableRow.addView(sale_price);
    tableRow.addView(expiry_date);


    holder.taskTextView.addView(tableRow);
    //holder.taskTextView.requestLayout();

  }

  @Override
  public int getItemCount() {
    return todoItems.size();
  }

  public synchronized  void addItem(final TodoItem todoItem){
    todoItems.add(todoItem);
    TodoAdapter.this.notifyDataSetChanged();
  }

  public synchronized void updateItem(final TodoItem todoItem) {
    if (todoItems.contains(todoItem)) {
      todoItems.set(todoItems.indexOf(todoItem), todoItem);
    } else {
      todoItems.add(todoItem);
    }
    TodoAdapter.this.notifyDataSetChanged();
  }

  public synchronized void refreshItemList(final Task<List<TodoItem>> getItemsTask) {
    getItemsTask.addOnCompleteListener(task -> {
      if (!task.isSuccessful()) {
        Log.e(TAG, "failed to get items", task.getException());
        return;
      }

      clearItems();
      final List<TodoItem> allItems = task.getResult();

      for (final TodoItem newItem : allItems) {
        todoItems.add(newItem);
      }

      notifyDataSetChanged();
    });
  }

  public synchronized void clearItems() {
    TodoAdapter.this.todoItems.clear();
  }

  // Callback for checkbox updates
  interface ItemUpdater {
    void updateChecked(ObjectId itemId, boolean isChecked);

    void updateTask(ObjectId itemId, TodoItem currentTask);
  }

  class TodoItemViewHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener,
          View.OnLongClickListener,
          CompoundButton.OnCheckedChangeListener {
    final TableLayout taskTextView;
    //final CheckBox taskCheckbox;

    TodoItemViewHolder(final View view) {
      super(view);

      taskTextView = view.findViewById(R.id.table);

      //taskTextView.requestLayout();
      //taskTextView = view.findViewWithTag(R.id.tv_task);
      //taskCheckbox = view.findViewById(R.id.cb_todo_checkbox);

      // Set listeners
      //taskCheckbox.setOnCheckedChangeListener(this);
      view.setOnClickListener(this);
      view.setOnLongClickListener(this);
      //taskCheckbox.setOnClickListener(this);
      //taskCheckbox.setOnLongClickListener(this);
    }

    @Override
    public synchronized void onCheckedChanged(
        final CompoundButton compoundButton,
        final boolean isChecked
    ) {
    }

    @Override
    public void onClick(final View view) {
      if (getAdapterPosition() == RecyclerView.NO_POSITION) {
        return;
      }
      final TodoItem item = todoItems.get(getAdapterPosition());
      //itemUpdater.updateChecked(item.get_id(), taskCheckbox.isChecked());
      itemUpdater.updateChecked(item.get_id(), false);
    }

    @Override
    public synchronized boolean onLongClick(final View view) {
      if (getAdapterPosition() == RecyclerView.NO_POSITION) {
        return false;
      }
      final TodoItem item = todoItems.get(getAdapterPosition());
      itemUpdater.updateTask(item.get_id(), item);
      return true;
    }
  }
}
