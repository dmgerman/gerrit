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
DECL|package|com.google.gerrit.acceptance.rest.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|rest
operator|.
name|change
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Maps
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
DECL|class|ReviewInput
specifier|public
class|class
name|ReviewInput
block|{
DECL|field|labels
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|labels
decl_stmt|;
DECL|method|approve ()
specifier|public
specifier|static
name|ReviewInput
name|approve
parameter_list|()
block|{
name|ReviewInput
name|in
init|=
operator|new
name|ReviewInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|labels
operator|=
name|Maps
operator|.
name|newHashMap
argument_list|()
expr_stmt|;
name|in
operator|.
name|labels
operator|.
name|put
argument_list|(
literal|"Code-Review"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
return|return
name|in
return|;
block|}
block|}
end_class

end_unit

