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
DECL|package|com.google.gerrit.httpd.raw
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|raw
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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|raw
operator|.
name|IndexHtmlUtil
operator|.
name|staticTemplateData
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
name|data
operator|.
name|SanitizedContent
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
name|data
operator|.
name|UnsafeSanitizedContentOrdainer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|IndexHtmlUtilTest
specifier|public
class|class
name|IndexHtmlUtilTest
block|{
annotation|@
name|Test
DECL|method|noPathAndNoCDN ()
specifier|public
name|void
name|noPathAndNoCDN
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|staticTemplateData
argument_list|(
literal|"http://example.com/"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
operator|new
name|HashMap
argument_list|<>
argument_list|()
argument_list|,
name|IndexHtmlUtilTest
operator|::
name|ordain
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"canonicalPath"
argument_list|,
literal|""
argument_list|,
literal|"polymer2"
argument_list|,
literal|"true"
argument_list|,
literal|"staticResourcePath"
argument_list|,
name|ordain
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|pathAndNoCDN ()
specifier|public
name|void
name|pathAndNoCDN
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|staticTemplateData
argument_list|(
literal|"http://example.com/gerrit/"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
operator|new
name|HashMap
argument_list|<>
argument_list|()
argument_list|,
name|IndexHtmlUtilTest
operator|::
name|ordain
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"canonicalPath"
argument_list|,
literal|"/gerrit"
argument_list|,
literal|"polymer2"
argument_list|,
literal|"true"
argument_list|,
literal|"staticResourcePath"
argument_list|,
name|ordain
argument_list|(
literal|"/gerrit"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|noPathAndCDN ()
specifier|public
name|void
name|noPathAndCDN
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|staticTemplateData
argument_list|(
literal|"http://example.com/"
argument_list|,
literal|"http://my-cdn.com/foo/bar/"
argument_list|,
literal|null
argument_list|,
operator|new
name|HashMap
argument_list|<>
argument_list|()
argument_list|,
name|IndexHtmlUtilTest
operator|::
name|ordain
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"canonicalPath"
argument_list|,
literal|""
argument_list|,
literal|"polymer2"
argument_list|,
literal|"true"
argument_list|,
literal|"staticResourcePath"
argument_list|,
name|ordain
argument_list|(
literal|"http://my-cdn.com/foo/bar/"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|pathAndCDN ()
specifier|public
name|void
name|pathAndCDN
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|staticTemplateData
argument_list|(
literal|"http://example.com/gerrit"
argument_list|,
literal|"http://my-cdn.com/foo/bar/"
argument_list|,
literal|null
argument_list|,
operator|new
name|HashMap
argument_list|<>
argument_list|()
argument_list|,
name|IndexHtmlUtilTest
operator|::
name|ordain
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"canonicalPath"
argument_list|,
literal|"/gerrit"
argument_list|,
literal|"polymer2"
argument_list|,
literal|"true"
argument_list|,
literal|"staticResourcePath"
argument_list|,
name|ordain
argument_list|(
literal|"http://my-cdn.com/foo/bar/"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|ordain (String s)
specifier|private
specifier|static
name|SanitizedContent
name|ordain
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|UnsafeSanitizedContentOrdainer
operator|.
name|ordainAsSafe
argument_list|(
name|s
argument_list|,
name|SanitizedContent
operator|.
name|ContentKind
operator|.
name|TRUSTED_RESOURCE_URI
argument_list|)
return|;
block|}
block|}
end_class

end_unit

