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
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDb
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
name|git
operator|.
name|GitRepositoryManager
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
name|patch
operator|.
name|PatchListCache
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
import|;
end_import

begin_comment
comment|/**  * Definition of a field stored in the secondary index.  *  * @param I input type from which documents are created and search results are  *     returned.  * @param T type that should be extracted from the input object when converting  *     to an index document.  */
end_comment

begin_class
DECL|class|FieldDef
specifier|public
specifier|abstract
class|class
name|FieldDef
parameter_list|<
name|I
parameter_list|,
name|T
parameter_list|>
block|{
comment|/** Definition of a single (non-repeatable) field. */
DECL|class|Single
specifier|public
specifier|static
specifier|abstract
class|class
name|Single
parameter_list|<
name|I
parameter_list|,
name|T
parameter_list|>
extends|extends
name|FieldDef
argument_list|<
name|I
argument_list|,
name|T
argument_list|>
block|{
DECL|method|Single (String name, FieldType<T> type, boolean stored)
name|Single
parameter_list|(
name|String
name|name
parameter_list|,
name|FieldType
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|boolean
name|stored
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|,
name|type
argument_list|,
name|stored
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isRepeatable ()
specifier|public
specifier|final
name|boolean
name|isRepeatable
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
comment|/** Definition of a repeatable field. */
DECL|class|Repeatable
specifier|public
specifier|static
specifier|abstract
class|class
name|Repeatable
parameter_list|<
name|I
parameter_list|,
name|T
parameter_list|>
extends|extends
name|FieldDef
argument_list|<
name|I
argument_list|,
name|Iterable
argument_list|<
name|T
argument_list|>
argument_list|>
block|{
DECL|method|Repeatable (String name, FieldType<T> type, boolean stored)
name|Repeatable
parameter_list|(
name|String
name|name
parameter_list|,
name|FieldType
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|boolean
name|stored
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|,
name|type
argument_list|,
name|stored
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isRepeatable ()
specifier|public
specifier|final
name|boolean
name|isRepeatable
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
comment|/** Arguments needed to fill in missing data in the input object. */
DECL|class|FillArgs
specifier|public
specifier|static
class|class
name|FillArgs
block|{
DECL|field|db
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
DECL|field|repoManager
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|patchListCache
specifier|final
name|PatchListCache
name|patchListCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|FillArgs (Provider<ReviewDb> db, GitRepositoryManager repoManager, PatchListCache patchListCache)
name|FillArgs
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|PatchListCache
name|patchListCache
parameter_list|)
block|{
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|patchListCache
operator|=
name|patchListCache
expr_stmt|;
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
DECL|method|FieldDef (String name, FieldType<?> type, boolean stored)
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
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|stored
operator|=
name|stored
expr_stmt|;
block|}
comment|/** @return name of the field. */
DECL|method|getName ()
specifier|public
specifier|final
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**    * @return type of the field; for repeatable fields, the inner type, not the    *     iterable type.    */
DECL|method|getType ()
specifier|public
specifier|final
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
specifier|final
name|boolean
name|isStored
parameter_list|()
block|{
return|return
name|stored
return|;
block|}
comment|/**    * Get the field contents from the input object.    *    * @param input input object.    * @param args arbitrary arguments needed to fill in indexable fields of the    *     input object.    * @return the field value(s) to index.    *    * @throws OrmException    */
DECL|method|get (I input, FillArgs args)
specifier|public
specifier|abstract
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
function_decl|;
comment|/** @return whether the field is repeatable. */
DECL|method|isRepeatable ()
specifier|public
specifier|abstract
name|boolean
name|isRepeatable
parameter_list|()
function_decl|;
block|}
end_class

end_unit

