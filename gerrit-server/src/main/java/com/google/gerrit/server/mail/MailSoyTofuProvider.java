begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
operator|.
name|CharStreams
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
name|io
operator|.
name|Resources
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
name|SitePaths
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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|ProvisionException
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
name|Singleton
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|template
operator|.
name|soy
operator|.
name|SoyFileSet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|template
operator|.
name|soy
operator|.
name|tofu
operator|.
name|SoyTofu
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
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_comment
comment|/** Configures Soy Tofu object for rendering email templates. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|MailSoyTofuProvider
specifier|public
class|class
name|MailSoyTofuProvider
implements|implements
name|Provider
argument_list|<
name|SoyTofu
argument_list|>
block|{
comment|// Note: will fail to construct the tofu object if this array is empty.
DECL|field|TEMPLATES
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|TEMPLATES
init|=
block|{
literal|"Abandoned.soy"
block|,
literal|"AddKey.soy"
block|,
literal|"ChangeSubject.soy"
block|,
literal|"ChangeFooter.soy"
block|,
literal|"Comment.soy"
block|,
literal|"CommentFooter.soy"
block|,
literal|"DeleteReviewer.soy"
block|,
literal|"DeleteVote.soy"
block|,
literal|"Footer.soy"
block|,
literal|"Merged.soy"
block|,
literal|"NewChange.soy"
block|,
literal|"RegisterNewEmail.soy"
block|,
literal|"ReplacePatchSet.soy"
block|,
literal|"Restored.soy"
block|,
literal|"Reverted.soy"
block|,   }
decl_stmt|;
DECL|field|site
specifier|private
specifier|final
name|SitePaths
name|site
decl_stmt|;
annotation|@
name|Inject
DECL|method|MailSoyTofuProvider (SitePaths site)
name|MailSoyTofuProvider
parameter_list|(
name|SitePaths
name|site
parameter_list|)
block|{
name|this
operator|.
name|site
operator|=
name|site
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|SoyTofu
name|get
parameter_list|()
throws|throws
name|ProvisionException
block|{
name|SoyFileSet
operator|.
name|Builder
name|builder
init|=
name|SoyFileSet
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|TEMPLATES
control|)
block|{
name|addTemplate
argument_list|(
name|builder
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
operator|.
name|compileToTofu
argument_list|()
return|;
block|}
DECL|method|addTemplate (SoyFileSet.Builder builder, String name)
specifier|private
name|void
name|addTemplate
parameter_list|(
name|SoyFileSet
operator|.
name|Builder
name|builder
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|ProvisionException
block|{
comment|// Load as a file in the mail templates directory if present.
name|Path
name|tmpl
init|=
name|site
operator|.
name|mail_dir
operator|.
name|resolve
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|Files
operator|.
name|isRegularFile
argument_list|(
name|tmpl
argument_list|)
condition|)
block|{
name|String
name|content
decl_stmt|;
try|try
init|(
name|Reader
name|r
init|=
name|Files
operator|.
name|newBufferedReader
argument_list|(
name|tmpl
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
init|)
block|{
name|content
operator|=
name|CharStreams
operator|.
name|toString
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"Failed to read template file "
operator|+
name|tmpl
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|err
argument_list|)
throw|;
block|}
name|builder
operator|.
name|add
argument_list|(
name|content
argument_list|,
name|tmpl
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// Otherwise load the template as a resource.
name|String
name|resourcePath
init|=
literal|"com/google/gerrit/server/mail/"
operator|+
name|name
decl_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|Resources
operator|.
name|getResource
argument_list|(
name|resourcePath
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

