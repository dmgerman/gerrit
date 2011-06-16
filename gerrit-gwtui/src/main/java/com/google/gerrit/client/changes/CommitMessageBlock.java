begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|changes
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
name|ui
operator|.
name|CommentLinkProcessor
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|Composite
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|HTML
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|ScrollPanel
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|safehtml
operator|.
name|client
operator|.
name|SafeHtml
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|safehtml
operator|.
name|client
operator|.
name|SafeHtmlBuilder
import|;
end_import

begin_class
DECL|class|CommitMessageBlock
specifier|public
class|class
name|CommitMessageBlock
extends|extends
name|Composite
block|{
DECL|field|description
specifier|private
specifier|final
name|HTML
name|description
decl_stmt|;
DECL|method|CommitMessageBlock ()
specifier|public
name|CommitMessageBlock
parameter_list|()
block|{
name|description
operator|=
operator|new
name|HTML
argument_list|()
expr_stmt|;
name|description
operator|.
name|setStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|changeScreenDescription
argument_list|()
argument_list|)
expr_stmt|;
name|initWidget
argument_list|(
name|description
argument_list|)
expr_stmt|;
block|}
DECL|method|display (final String commitMessage)
specifier|public
name|void
name|display
parameter_list|(
specifier|final
name|String
name|commitMessage
parameter_list|)
block|{
name|SafeHtml
name|msg
init|=
operator|new
name|SafeHtmlBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|commitMessage
argument_list|)
decl_stmt|;
name|msg
operator|=
name|msg
operator|.
name|linkify
argument_list|()
expr_stmt|;
name|msg
operator|=
name|CommentLinkProcessor
operator|.
name|apply
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|msg
operator|=
operator|new
name|SafeHtmlBuilder
argument_list|()
operator|.
name|openElement
argument_list|(
literal|"p"
argument_list|)
operator|.
name|append
argument_list|(
name|msg
argument_list|)
operator|.
name|closeElement
argument_list|(
literal|"p"
argument_list|)
expr_stmt|;
name|msg
operator|=
name|msg
operator|.
name|replaceAll
argument_list|(
literal|"\n\n"
argument_list|,
literal|"</p><p>"
argument_list|)
expr_stmt|;
name|msg
operator|=
name|msg
operator|.
name|replaceAll
argument_list|(
literal|"\n"
argument_list|,
literal|"<br />"
argument_list|)
expr_stmt|;
name|SafeHtml
operator|.
name|set
argument_list|(
name|description
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

