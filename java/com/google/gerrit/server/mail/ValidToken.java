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
DECL|package|com.google.gerrit.server.mail
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|mail
package|;
end_package

begin_comment
comment|/** A validated token from {@link SignedToken#checkToken(String, String)} */
end_comment

begin_class
DECL|class|ValidToken
class|class
name|ValidToken
block|{
DECL|field|refresh
specifier|private
specifier|final
name|boolean
name|refresh
decl_stmt|;
DECL|field|data
specifier|private
specifier|final
name|String
name|data
decl_stmt|;
DECL|method|ValidToken (final boolean ref, final String d)
name|ValidToken
parameter_list|(
specifier|final
name|boolean
name|ref
parameter_list|,
specifier|final
name|String
name|d
parameter_list|)
block|{
name|refresh
operator|=
name|ref
expr_stmt|;
name|data
operator|=
name|d
expr_stmt|;
block|}
comment|/** The text protected by the token's encryption key. */
DECL|method|getData ()
name|String
name|getData
parameter_list|()
block|{
return|return
name|data
return|;
block|}
comment|/** True if the token's life span is almost half-over and should be renewed. */
DECL|method|needsRefresh ()
name|boolean
name|needsRefresh
parameter_list|()
block|{
return|return
name|refresh
return|;
block|}
block|}
end_class

end_unit

