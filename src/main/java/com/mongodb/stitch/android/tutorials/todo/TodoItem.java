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

import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonObjectId;
import org.bson.BsonReader;
import org.bson.BsonString;
import org.bson.BsonWriter;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;
import org.json.JSONObject;

class TodoItem {

  public static final String TODO_DATABASE = "todo";
  public static final String TODO_ITEMS_COLLECTION = "items";

  private final ObjectId _id;
  private final String owner_id;
  private final String store_name;
  private final String store_location;
  private final String item_description;
  private final int item_quantity;
  private final double original_price;
  private final double sale_price;
  private final boolean checked;

  /** Constructs a todo item from a MongoDB document. */
  TodoItem(
    final ObjectId id,
    final String owner_id,
    final String store_name,
    final String store_location,
    final String item_description,
    final int item_quantity,
    final double original_price,
    final double sale_price,
    final boolean checked
  ) {
    this._id = id;
    this.owner_id = owner_id;
    this.store_name = store_name;
    this.store_location = store_location;
    this.item_description = item_description;
    this.item_quantity = item_quantity;
    this.original_price = original_price;
    this.sale_price = sale_price;
    this.checked = false;
  }

  public ObjectId get_id() {
    return _id;
  }

  public String getStore_name() {
    return store_name;
  }

  public String getStore_location() {
    return store_location;
  }

  public String getItem_description() {
    return item_description;
  }

  public int getItem_quantity() {
    return item_quantity;
  }

  public double getOriginal_price() {
    return original_price;
  }

  public double getSale_price() {
    return sale_price;
  }

  public String getOwner_id() {
    return owner_id;
  }

  public Boolean isChecked() {
    return checked;
  }

  static BsonDocument toBsonDocument(final TodoItem item) {
    final BsonDocument asDoc = new BsonDocument();
    asDoc.put(Fields.ID, new BsonObjectId(item.get_id()));
    asDoc.put(Fields.STORE_NAME, new BsonString(item.getStore_name()));
    asDoc.put(Fields.STORE_LOCATION, new BsonString(item.getStore_location()));
    asDoc.put(Fields.ITEM_DESCRIPTION, new BsonString(item.getItem_description()));
    asDoc.put(Fields.ITEM_QUANTITY, new BsonInt32(item.getItem_quantity()));
    asDoc.put(Fields.ORIGINAL_PRICE, new BsonDouble(item.getOriginal_price()));
    asDoc.put(Fields.SALE_PRICE, new BsonDouble(item.getSale_price()));
    asDoc.put(Fields.OWNER_ID, new BsonString(item.getOwner_id()));
    asDoc.put(Fields.CHECKED, new BsonBoolean(item.isChecked()));
    return asDoc;
  }

  static TodoItem fromBsonDocument(final BsonDocument doc) {
    return new TodoItem(
        doc.getObjectId(Fields.ID).getValue(),
        doc.getString(Fields.OWNER_ID).getValue(),
        doc.getString(Fields.STORE_NAME).getValue(),
        doc.getString(Fields.STORE_LOCATION).getValue(),
        doc.getString(Fields.ITEM_DESCRIPTION).getValue(),
        doc.getInt32(Fields.ITEM_QUANTITY).getValue(),
        doc.getDouble(Fields.ORIGINAL_PRICE).getValue(),
        doc.getDouble(Fields.SALE_PRICE).getValue(),
        doc.getBoolean(Fields.CHECKED).getValue()
    );
  }

  static final class Fields {
    static final String ID = "_id";
    static final String STORE_NAME = "store_name";
    static final String STORE_LOCATION = "store_location";
    static final String ITEM_DESCRIPTION = "item_description";
    static final String ITEM_QUANTITY = "item_quantity";
    static final String ORIGINAL_PRICE = "original_price";
    static final String SALE_PRICE = "sale_price";
    static final String OWNER_ID = "owner_id";
    static final String CHECKED = "checked";
  }

  public static final Codec<TodoItem> codec = new Codec<TodoItem>() {

    @Override
    public void encode(
        final BsonWriter writer, final TodoItem value, final EncoderContext encoderContext) {
      new BsonDocumentCodec().encode(writer, toBsonDocument(value), encoderContext);
    }

    @Override
    public Class<TodoItem> getEncoderClass() {
      return TodoItem.class;
    }

    @Override
    public TodoItem decode(
        final BsonReader reader, final DecoderContext decoderContext) {
      final BsonDocument document = (new BsonDocumentCodec()).decode(reader, decoderContext);
      return fromBsonDocument(document);
    }
  };
}
