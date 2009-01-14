begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2009 Google Inc.
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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|client
operator|.
name|reviewdb
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
name|gwtorm
operator|.
name|client
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|util
operator|.
name|Base64
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|util
operator|.
name|NB
import|;
end_import

begin_class
DECL|class|ChangeUtil
specifier|public
class|class
name|ChangeUtil
block|{
DECL|field|uuidPrefix
specifier|private
specifier|static
name|int
name|uuidPrefix
decl_stmt|;
DECL|field|uuidSeq
specifier|private
specifier|static
name|int
name|uuidSeq
decl_stmt|;
comment|/**    * Generate a new unique identifier for change message entities.    *     * @param db the database connection, used to increment the change message    *        allocation sequence.    * @return the new unique identifier.    * @throws OrmException the database couldn't be incremented.    */
DECL|method|messageUUID (final ReviewDb db)
specifier|public
specifier|static
name|String
name|messageUUID
parameter_list|(
specifier|final
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|byte
index|[]
name|raw
init|=
operator|new
name|byte
index|[
literal|8
index|]
decl_stmt|;
name|fill
argument_list|(
name|raw
argument_list|,
name|db
argument_list|)
expr_stmt|;
return|return
name|Base64
operator|.
name|encodeBytes
argument_list|(
name|raw
argument_list|)
return|;
block|}
DECL|method|fill (byte[] raw, ReviewDb db)
specifier|private
specifier|static
specifier|synchronized
name|void
name|fill
parameter_list|(
name|byte
index|[]
name|raw
parameter_list|,
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
name|uuidSeq
operator|==
literal|0
condition|)
block|{
name|uuidPrefix
operator|=
name|db
operator|.
name|nextChangeMessageId
argument_list|()
expr_stmt|;
name|uuidSeq
operator|=
name|Integer
operator|.
name|MAX_VALUE
expr_stmt|;
block|}
name|NB
operator|.
name|encodeInt32
argument_list|(
name|raw
argument_list|,
literal|0
argument_list|,
name|uuidPrefix
argument_list|)
expr_stmt|;
name|NB
operator|.
name|encodeInt32
argument_list|(
name|raw
argument_list|,
literal|4
argument_list|,
name|uuidSeq
operator|--
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

