begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|git
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
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|AnyObjectId
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
name|Constants
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
name|ObjectReader
import|;
end_import

begin_comment
comment|/** Static utilities for working with {@code ObjectId}s. */
end_comment

begin_class
DECL|class|ObjectIds
specifier|public
class|class
name|ObjectIds
block|{
comment|/** Length of a binary SHA-1 byte array. */
DECL|field|LEN
specifier|public
specifier|static
specifier|final
name|int
name|LEN
init|=
name|Constants
operator|.
name|OBJECT_ID_LENGTH
decl_stmt|;
comment|/** Length of a hex SHA-1 string. */
DECL|field|STR_LEN
specifier|public
specifier|static
specifier|final
name|int
name|STR_LEN
init|=
name|Constants
operator|.
name|OBJECT_ID_STRING_LENGTH
decl_stmt|;
comment|/** Default abbreviated length of a hex SHA-1 string. */
DECL|field|ABBREV_STR_LEN
specifier|public
specifier|static
specifier|final
name|int
name|ABBREV_STR_LEN
init|=
literal|7
decl_stmt|;
comment|/**    * Abbreviate an ID's hex string representation to 7 chars.    *    * @param id object ID.    * @return abbreviated hex string representation, exactly 7 chars.    */
DECL|method|abbreviateName (AnyObjectId id)
specifier|public
specifier|static
name|String
name|abbreviateName
parameter_list|(
name|AnyObjectId
name|id
parameter_list|)
block|{
return|return
name|abbreviateName
argument_list|(
name|id
argument_list|,
name|ABBREV_STR_LEN
argument_list|)
return|;
block|}
comment|/**    * Abbreviate an ID's hex string representation to {@code n} chars.    *    * @param id object ID.    * @param n number of hex chars, 1 to 40.    * @return abbreviated hex string representation, exactly {@code n} chars.    */
DECL|method|abbreviateName (AnyObjectId id, int n)
specifier|public
specifier|static
name|String
name|abbreviateName
parameter_list|(
name|AnyObjectId
name|id
parameter_list|,
name|int
name|n
parameter_list|)
block|{
name|checkValidLength
argument_list|(
name|n
argument_list|)
expr_stmt|;
return|return
name|requireNonNull
argument_list|(
name|id
argument_list|)
operator|.
name|abbreviate
argument_list|(
name|n
argument_list|)
operator|.
name|name
argument_list|()
return|;
block|}
comment|/**    * Abbreviate an ID's hex string representation uniquely to at least 7 chars.    *    * @param id object ID.    * @param reader object reader for determining uniqueness.    * @return abbreviated hex string representation, unique according to {@code reader} at least 7    *     chars.    * @throws IOException if an error occurs while looking for ambiguous objects.    */
DECL|method|abbreviateName (AnyObjectId id, ObjectReader reader)
specifier|public
specifier|static
name|String
name|abbreviateName
parameter_list|(
name|AnyObjectId
name|id
parameter_list|,
name|ObjectReader
name|reader
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|abbreviateName
argument_list|(
name|id
argument_list|,
name|ABBREV_STR_LEN
argument_list|,
name|reader
argument_list|)
return|;
block|}
comment|/**    * Abbreviate an ID's hex string representation uniquely to at least {@code n} chars.    *    * @param id object ID.    * @param n minimum number of hex chars, 1 to 40.    * @param reader object reader for determining uniqueness.    * @return abbreviated hex string representation, unique according to {@code reader} at least    *     {@code n} chars.    * @throws IOException if an error occurs while looking for ambiguous objects.    */
DECL|method|abbreviateName (AnyObjectId id, int n, ObjectReader reader)
specifier|public
specifier|static
name|String
name|abbreviateName
parameter_list|(
name|AnyObjectId
name|id
parameter_list|,
name|int
name|n
parameter_list|,
name|ObjectReader
name|reader
parameter_list|)
throws|throws
name|IOException
block|{
name|checkValidLength
argument_list|(
name|n
argument_list|)
expr_stmt|;
return|return
name|reader
operator|.
name|abbreviate
argument_list|(
name|id
argument_list|,
name|n
argument_list|)
operator|.
name|name
argument_list|()
return|;
block|}
DECL|method|checkValidLength (int n)
specifier|private
specifier|static
name|void
name|checkValidLength
parameter_list|(
name|int
name|n
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|n
operator|>
literal|0
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|n
operator|<=
name|STR_LEN
argument_list|)
expr_stmt|;
block|}
DECL|method|ObjectIds ()
specifier|private
name|ObjectIds
parameter_list|()
block|{}
block|}
end_class

end_unit

