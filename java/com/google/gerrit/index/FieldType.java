begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.index
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|index
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_comment
comment|/** Document field types supported by the secondary index system. */
end_comment

begin_class
DECL|class|FieldType
specifier|public
class|class
name|FieldType
parameter_list|<
name|T
parameter_list|>
block|{
comment|/** A single integer-valued field. */
DECL|field|INTEGER
specifier|public
specifier|static
specifier|final
name|FieldType
argument_list|<
name|Integer
argument_list|>
name|INTEGER
init|=
operator|new
name|FieldType
argument_list|<>
argument_list|(
literal|"INTEGER"
argument_list|)
decl_stmt|;
comment|/** A single-integer-valued field matched using range queries. */
DECL|field|INTEGER_RANGE
specifier|public
specifier|static
specifier|final
name|FieldType
argument_list|<
name|Integer
argument_list|>
name|INTEGER_RANGE
init|=
operator|new
name|FieldType
argument_list|<>
argument_list|(
literal|"INTEGER_RANGE"
argument_list|)
decl_stmt|;
comment|/** A single integer-valued field. */
DECL|field|LONG
specifier|public
specifier|static
specifier|final
name|FieldType
argument_list|<
name|Long
argument_list|>
name|LONG
init|=
operator|new
name|FieldType
argument_list|<>
argument_list|(
literal|"LONG"
argument_list|)
decl_stmt|;
comment|/** A single date/time-valued field. */
DECL|field|TIMESTAMP
specifier|public
specifier|static
specifier|final
name|FieldType
argument_list|<
name|Timestamp
argument_list|>
name|TIMESTAMP
init|=
operator|new
name|FieldType
argument_list|<>
argument_list|(
literal|"TIMESTAMP"
argument_list|)
decl_stmt|;
comment|/** A string field searched using exact-match semantics. */
DECL|field|EXACT
specifier|public
specifier|static
specifier|final
name|FieldType
argument_list|<
name|String
argument_list|>
name|EXACT
init|=
operator|new
name|FieldType
argument_list|<>
argument_list|(
literal|"EXACT"
argument_list|)
decl_stmt|;
comment|/** A string field searched using prefix. */
DECL|field|PREFIX
specifier|public
specifier|static
specifier|final
name|FieldType
argument_list|<
name|String
argument_list|>
name|PREFIX
init|=
operator|new
name|FieldType
argument_list|<>
argument_list|(
literal|"PREFIX"
argument_list|)
decl_stmt|;
comment|/** A string field searched using fuzzy-match semantics. */
DECL|field|FULL_TEXT
specifier|public
specifier|static
specifier|final
name|FieldType
argument_list|<
name|String
argument_list|>
name|FULL_TEXT
init|=
operator|new
name|FieldType
argument_list|<>
argument_list|(
literal|"FULL_TEXT"
argument_list|)
decl_stmt|;
comment|/** A field that is only stored as raw bytes and cannot be queried. */
DECL|field|STORED_ONLY
specifier|public
specifier|static
specifier|final
name|FieldType
argument_list|<
name|byte
index|[]
argument_list|>
name|STORED_ONLY
init|=
operator|new
name|FieldType
argument_list|<>
argument_list|(
literal|"STORED_ONLY"
argument_list|)
decl_stmt|;
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|method|FieldType (String name)
specifier|private
name|FieldType
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|badFieldType (FieldType<?> t)
specifier|public
specifier|static
name|IllegalArgumentException
name|badFieldType
parameter_list|(
name|FieldType
argument_list|<
name|?
argument_list|>
name|t
parameter_list|)
block|{
return|return
operator|new
name|IllegalArgumentException
argument_list|(
literal|"unknown index field type "
operator|+
name|t
argument_list|)
return|;
block|}
block|}
end_class

end_unit

