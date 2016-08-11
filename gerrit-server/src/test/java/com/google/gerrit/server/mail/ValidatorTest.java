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
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assert_
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_class
DECL|class|ValidatorTest
specifier|public
class|class
name|ValidatorTest
block|{
DECL|field|UNSUPPORTED_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|UNSUPPORTED_PREFIX
init|=
literal|"#! "
decl_stmt|;
annotation|@
name|Test
DECL|method|validateLocalDomain ()
specifier|public
name|void
name|validateLocalDomain
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|OutgoingEmailValidator
operator|.
name|isValid
argument_list|(
literal|"foo@bar.local"
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|validateTopLevelDomains ()
specifier|public
name|void
name|validateTopLevelDomains
parameter_list|()
throws|throws
name|Exception
block|{
try|try
init|(
name|InputStream
name|in
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"tlds-alpha-by-domain.txt"
argument_list|)
init|)
block|{
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"TLD list not found"
argument_list|)
throw|;
block|}
name|BufferedReader
name|r
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|in
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|tld
decl_stmt|;
while|while
condition|(
operator|(
name|tld
operator|=
name|r
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|tld
operator|.
name|startsWith
argument_list|(
literal|"# "
argument_list|)
operator|||
name|tld
operator|.
name|startsWith
argument_list|(
literal|"XN--"
argument_list|)
condition|)
block|{
comment|// Ignore comments and non-latin domains
continue|continue;
block|}
if|if
condition|(
name|tld
operator|.
name|startsWith
argument_list|(
name|UNSUPPORTED_PREFIX
argument_list|)
condition|)
block|{
name|String
name|test
init|=
literal|"test@example."
operator|+
name|tld
operator|.
name|toLowerCase
argument_list|()
operator|.
name|substring
argument_list|(
name|UNSUPPORTED_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|assert_
argument_list|()
operator|.
name|withFailureMessage
argument_list|(
literal|"expected invalid TLD \""
operator|+
name|test
operator|+
literal|"\""
argument_list|)
operator|.
name|that
argument_list|(
name|OutgoingEmailValidator
operator|.
name|isValid
argument_list|(
name|test
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|String
name|test
init|=
literal|"test@example."
operator|+
name|tld
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
name|assert_
argument_list|()
operator|.
name|withFailureMessage
argument_list|(
literal|"failed to validate TLD \""
operator|+
name|test
operator|+
literal|"\""
argument_list|)
operator|.
name|that
argument_list|(
name|OutgoingEmailValidator
operator|.
name|isValid
argument_list|(
name|test
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

