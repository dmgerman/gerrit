begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.ui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|ui
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
name|Gerrit
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
name|client
operator|.
name|admin
operator|.
name|Util
import|;
end_import

begin_class
DECL|class|PagingHyperlink
specifier|public
class|class
name|PagingHyperlink
extends|extends
name|Hyperlink
block|{
DECL|method|createPrev ()
specifier|public
specifier|static
name|PagingHyperlink
name|createPrev
parameter_list|()
block|{
return|return
operator|new
name|PagingHyperlink
argument_list|(
name|Util
operator|.
name|C
operator|.
name|pagedListPrev
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createNext ()
specifier|public
specifier|static
name|PagingHyperlink
name|createNext
parameter_list|()
block|{
return|return
operator|new
name|PagingHyperlink
argument_list|(
name|Util
operator|.
name|C
operator|.
name|pagedListNext
argument_list|()
argument_list|)
return|;
block|}
DECL|method|PagingHyperlink (String text)
specifier|private
name|PagingHyperlink
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|super
argument_list|(
name|text
argument_list|,
literal|true
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|setStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|pagingLink
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

