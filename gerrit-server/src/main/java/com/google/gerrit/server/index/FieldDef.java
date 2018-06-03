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
DECL|package|com.google.gerrit.server.index
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|index
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|CharMatcher
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|AllUsersName
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|GerritServerConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|TrackingFooters
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_comment
comment|/**  * Definition of a field stored in the secondary index.  *  * @param<I> input type from which documents are created and search results are returned.  * @param<T> type that should be extracted from the input object when converting to an index  *     document.  */
end_comment

begin_class
DECL|class|FieldDef
specifier|public
specifier|final
class|class
name|FieldDef
parameter_list|<
name|I
parameter_list|,
name|T
parameter_list|>
block|{
DECL|method|exact (String name)
specifier|public
specifier|static
name|FieldDef
operator|.
name|Builder
argument_list|<
name|String
argument_list|>
name|exact
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|FieldDef
operator|.
name|Builder
argument_list|<>
argument_list|(
name|FieldType
operator|.
name|EXACT
argument_list|,
name|name
argument_list|)
return|;
block|}
DECL|method|keyword (String name)
specifier|public
specifier|static
name|FieldDef
operator|.
name|Builder
argument_list|<
name|String
argument_list|>
name|keyword
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|FieldDef
operator|.
name|Builder
argument_list|<>
argument_list|(
name|FieldType
operator|.
name|KEYWORD
argument_list|,
name|name
argument_list|)
return|;
block|}
DECL|method|fullText (String name)
specifier|public
specifier|static
name|FieldDef
operator|.
name|Builder
argument_list|<
name|String
argument_list|>
name|fullText
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|FieldDef
operator|.
name|Builder
argument_list|<>
argument_list|(
name|FieldType
operator|.
name|FULL_TEXT
argument_list|,
name|name
argument_list|)
return|;
block|}
DECL|method|intRange (String name)
specifier|public
specifier|static
name|FieldDef
operator|.
name|Builder
argument_list|<
name|Integer
argument_list|>
name|intRange
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|FieldDef
operator|.
name|Builder
argument_list|<>
argument_list|(
name|FieldType
operator|.
name|INTEGER_RANGE
argument_list|,
name|name
argument_list|)
operator|.
name|stored
argument_list|()
return|;
block|}
DECL|method|integer (String name)
specifier|public
specifier|static
name|FieldDef
operator|.
name|Builder
argument_list|<
name|Integer
argument_list|>
name|integer
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|FieldDef
operator|.
name|Builder
argument_list|<>
argument_list|(
name|FieldType
operator|.
name|INTEGER
argument_list|,
name|name
argument_list|)
return|;
block|}
DECL|method|prefix (String name)
specifier|public
specifier|static
name|FieldDef
operator|.
name|Builder
argument_list|<
name|String
argument_list|>
name|prefix
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|FieldDef
operator|.
name|Builder
argument_list|<>
argument_list|(
name|FieldType
operator|.
name|PREFIX
argument_list|,
name|name
argument_list|)
return|;
block|}
DECL|method|storedOnly (String name)
specifier|public
specifier|static
name|FieldDef
operator|.
name|Builder
argument_list|<
name|byte
index|[]
argument_list|>
name|storedOnly
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|FieldDef
operator|.
name|Builder
argument_list|<>
argument_list|(
name|FieldType
operator|.
name|STORED_ONLY
argument_list|,
name|name
argument_list|)
operator|.
name|stored
argument_list|()
return|;
block|}
DECL|method|timestamp (String name)
specifier|public
specifier|static
name|FieldDef
operator|.
name|Builder
argument_list|<
name|Timestamp
argument_list|>
name|timestamp
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|FieldDef
operator|.
name|Builder
argument_list|<>
argument_list|(
name|FieldType
operator|.
name|TIMESTAMP
argument_list|,
name|name
argument_list|)
return|;
block|}
annotation|@
name|FunctionalInterface
DECL|interface|Getter
specifier|public
interface|interface
name|Getter
parameter_list|<
name|I
parameter_list|,
name|T
parameter_list|>
block|{
DECL|method|get (I input)
name|T
name|get
parameter_list|(
name|I
name|input
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
function_decl|;
block|}
annotation|@
name|FunctionalInterface
DECL|interface|GetterWithArgs
specifier|public
interface|interface
name|GetterWithArgs
parameter_list|<
name|I
parameter_list|,
name|T
parameter_list|>
block|{
DECL|method|get (I input, FillArgs args)
name|T
name|get
parameter_list|(
name|I
name|input
parameter_list|,
name|FillArgs
name|args
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
function_decl|;
block|}
comment|/** Arguments needed to fill in missing data in the input object. */
DECL|class|FillArgs
specifier|public
specifier|static
class|class
name|FillArgs
block|{
DECL|field|trackingFooters
specifier|public
specifier|final
name|TrackingFooters
name|trackingFooters
decl_stmt|;
DECL|field|allowsDrafts
specifier|public
specifier|final
name|boolean
name|allowsDrafts
decl_stmt|;
DECL|field|allUsers
specifier|public
specifier|final
name|AllUsersName
name|allUsers
decl_stmt|;
annotation|@
name|Inject
DECL|method|FillArgs ( TrackingFooters trackingFooters, @GerritServerConfig Config cfg, AllUsersName allUsers)
name|FillArgs
parameter_list|(
name|TrackingFooters
name|trackingFooters
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
name|AllUsersName
name|allUsers
parameter_list|)
block|{
name|this
operator|.
name|trackingFooters
operator|=
name|trackingFooters
expr_stmt|;
name|this
operator|.
name|allowsDrafts
operator|=
name|cfg
operator|==
literal|null
condition|?
literal|true
else|:
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"change"
argument_list|,
literal|"allowDrafts"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|allUsers
operator|=
name|allUsers
expr_stmt|;
block|}
block|}
DECL|class|Builder
specifier|public
specifier|static
class|class
name|Builder
parameter_list|<
name|T
parameter_list|>
block|{
DECL|field|type
specifier|private
specifier|final
name|FieldType
argument_list|<
name|T
argument_list|>
name|type
decl_stmt|;
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|stored
specifier|private
name|boolean
name|stored
decl_stmt|;
DECL|method|Builder (FieldType<T> type, String name)
specifier|public
name|Builder
parameter_list|(
name|FieldType
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|checkNotNull
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|checkNotNull
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
DECL|method|stored ()
specifier|public
name|Builder
argument_list|<
name|T
argument_list|>
name|stored
parameter_list|()
block|{
name|this
operator|.
name|stored
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|build (Getter<I, T> getter)
specifier|public
parameter_list|<
name|I
parameter_list|>
name|FieldDef
argument_list|<
name|I
argument_list|,
name|T
argument_list|>
name|build
parameter_list|(
name|Getter
argument_list|<
name|I
argument_list|,
name|T
argument_list|>
name|getter
parameter_list|)
block|{
return|return
name|build
argument_list|(
parameter_list|(
name|in
parameter_list|,
name|a
parameter_list|)
lambda|->
name|getter
operator|.
name|get
argument_list|(
name|in
argument_list|)
argument_list|)
return|;
block|}
DECL|method|build (GetterWithArgs<I, T> getter)
specifier|public
parameter_list|<
name|I
parameter_list|>
name|FieldDef
argument_list|<
name|I
argument_list|,
name|T
argument_list|>
name|build
parameter_list|(
name|GetterWithArgs
argument_list|<
name|I
argument_list|,
name|T
argument_list|>
name|getter
parameter_list|)
block|{
return|return
operator|new
name|FieldDef
argument_list|<>
argument_list|(
name|name
argument_list|,
name|type
argument_list|,
name|stored
argument_list|,
literal|false
argument_list|,
name|getter
argument_list|)
return|;
block|}
DECL|method|buildRepeatable (Getter<I, Iterable<T>> getter)
specifier|public
parameter_list|<
name|I
parameter_list|>
name|FieldDef
argument_list|<
name|I
argument_list|,
name|Iterable
argument_list|<
name|T
argument_list|>
argument_list|>
name|buildRepeatable
parameter_list|(
name|Getter
argument_list|<
name|I
argument_list|,
name|Iterable
argument_list|<
name|T
argument_list|>
argument_list|>
name|getter
parameter_list|)
block|{
return|return
name|buildRepeatable
argument_list|(
parameter_list|(
name|in
parameter_list|,
name|a
parameter_list|)
lambda|->
name|getter
operator|.
name|get
argument_list|(
name|in
argument_list|)
argument_list|)
return|;
block|}
DECL|method|buildRepeatable (GetterWithArgs<I, Iterable<T>> getter)
specifier|public
parameter_list|<
name|I
parameter_list|>
name|FieldDef
argument_list|<
name|I
argument_list|,
name|Iterable
argument_list|<
name|T
argument_list|>
argument_list|>
name|buildRepeatable
parameter_list|(
name|GetterWithArgs
argument_list|<
name|I
argument_list|,
name|Iterable
argument_list|<
name|T
argument_list|>
argument_list|>
name|getter
parameter_list|)
block|{
return|return
operator|new
name|FieldDef
argument_list|<>
argument_list|(
name|name
argument_list|,
name|type
argument_list|,
name|stored
argument_list|,
literal|true
argument_list|,
name|getter
argument_list|)
return|;
block|}
block|}
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|type
specifier|private
specifier|final
name|FieldType
argument_list|<
name|?
argument_list|>
name|type
decl_stmt|;
DECL|field|stored
specifier|private
specifier|final
name|boolean
name|stored
decl_stmt|;
DECL|field|repeatable
specifier|private
specifier|final
name|boolean
name|repeatable
decl_stmt|;
DECL|field|getter
specifier|private
specifier|final
name|GetterWithArgs
argument_list|<
name|I
argument_list|,
name|T
argument_list|>
name|getter
decl_stmt|;
DECL|method|FieldDef ( String name, FieldType<?> type, boolean stored, boolean repeatable, GetterWithArgs<I, T> getter)
specifier|private
name|FieldDef
parameter_list|(
name|String
name|name
parameter_list|,
name|FieldType
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|boolean
name|stored
parameter_list|,
name|boolean
name|repeatable
parameter_list|,
name|GetterWithArgs
argument_list|<
name|I
argument_list|,
name|T
argument_list|>
name|getter
parameter_list|)
block|{
name|checkArgument
argument_list|(
operator|!
operator|(
name|repeatable
operator|&&
name|type
operator|==
name|FieldType
operator|.
name|INTEGER_RANGE
operator|)
argument_list|,
literal|"Range queries against repeated fields are unsupported"
argument_list|)
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|checkName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|checkNotNull
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|this
operator|.
name|stored
operator|=
name|stored
expr_stmt|;
name|this
operator|.
name|repeatable
operator|=
name|repeatable
expr_stmt|;
name|this
operator|.
name|getter
operator|=
name|checkNotNull
argument_list|(
name|getter
argument_list|)
expr_stmt|;
block|}
DECL|method|checkName (String name)
specifier|private
specifier|static
name|String
name|checkName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|CharMatcher
name|m
init|=
name|CharMatcher
operator|.
name|anyOf
argument_list|(
literal|"abcdefghijklmnopqrstuvwxyz0123456789_"
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|name
operator|!=
literal|null
operator|&&
name|m
operator|.
name|matchesAllOf
argument_list|(
name|name
argument_list|)
argument_list|,
literal|"illegal field name: %s"
argument_list|,
name|name
argument_list|)
expr_stmt|;
return|return
name|name
return|;
block|}
comment|/** @return name of the field. */
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
comment|/** @return type of the field; for repeatable fields, the inner type, not the iterable type. */
DECL|method|getType ()
specifier|public
name|FieldType
argument_list|<
name|?
argument_list|>
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
comment|/** @return whether the field should be stored in the index. */
DECL|method|isStored ()
specifier|public
name|boolean
name|isStored
parameter_list|()
block|{
return|return
name|stored
return|;
block|}
comment|/**    * Get the field contents from the input object.    *    * @param input input object.    * @param args arbitrary arguments needed to fill in indexable fields of the input object.    * @return the field value(s) to index.    * @throws OrmException    */
DECL|method|get (I input, FillArgs args)
specifier|public
name|T
name|get
parameter_list|(
name|I
name|input
parameter_list|,
name|FillArgs
name|args
parameter_list|)
throws|throws
name|OrmException
block|{
try|try
block|{
return|return
name|getter
operator|.
name|get
argument_list|(
name|input
argument_list|,
name|args
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/** @return whether the field is repeatable. */
DECL|method|isRepeatable ()
specifier|public
name|boolean
name|isRepeatable
parameter_list|()
block|{
return|return
name|repeatable
return|;
block|}
block|}
end_class

end_unit

