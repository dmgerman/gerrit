begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.client.rpc
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|rpc
package|;
end_package

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
name|Key
import|;
end_import

begin_comment
comment|/** Error indicating the entity's database records are invalid. */
end_comment

begin_class
DECL|class|CorruptEntityException
specifier|public
class|class
name|CorruptEntityException
extends|extends
name|Exception
block|{
DECL|field|MESSAGE_PREFIX
specifier|public
specifier|static
specifier|final
name|String
name|MESSAGE_PREFIX
init|=
literal|"Corrupt Database Entity: "
decl_stmt|;
DECL|method|CorruptEntityException (final Key<?> key)
specifier|public
name|CorruptEntityException
parameter_list|(
specifier|final
name|Key
argument_list|<
name|?
argument_list|>
name|key
parameter_list|)
block|{
name|super
argument_list|(
name|MESSAGE_PREFIX
operator|+
name|key
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

