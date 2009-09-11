begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
DECL|class|AddressTest
specifier|public
class|class
name|AddressTest
extends|extends
name|TestCase
block|{
DECL|method|testParse_NameEmail1 ()
specifier|public
name|void
name|testParse_NameEmail1
parameter_list|()
block|{
specifier|final
name|Address
name|a
init|=
name|Address
operator|.
name|parse
argument_list|(
literal|"A U Thor<author@example.com>"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"A U Thor"
argument_list|,
name|a
operator|.
name|name
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"author@example.com"
argument_list|,
name|a
operator|.
name|email
argument_list|)
expr_stmt|;
block|}
DECL|method|testParse_NameEmail2 ()
specifier|public
name|void
name|testParse_NameEmail2
parameter_list|()
block|{
specifier|final
name|Address
name|a
init|=
name|Address
operator|.
name|parse
argument_list|(
literal|"A<a@b>"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"A"
argument_list|,
name|a
operator|.
name|name
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a@b"
argument_list|,
name|a
operator|.
name|email
argument_list|)
expr_stmt|;
block|}
DECL|method|testParse_NameEmail3 ()
specifier|public
name|void
name|testParse_NameEmail3
parameter_list|()
block|{
specifier|final
name|Address
name|a
init|=
name|Address
operator|.
name|parse
argument_list|(
literal|"<a@b>"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|a
operator|.
name|name
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a@b"
argument_list|,
name|a
operator|.
name|email
argument_list|)
expr_stmt|;
block|}
DECL|method|testParse_NameEmail4 ()
specifier|public
name|void
name|testParse_NameEmail4
parameter_list|()
block|{
specifier|final
name|Address
name|a
init|=
name|Address
operator|.
name|parse
argument_list|(
literal|"A U Thor<author@example.com>"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"A U Thor"
argument_list|,
name|a
operator|.
name|name
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"author@example.com"
argument_list|,
name|a
operator|.
name|email
argument_list|)
expr_stmt|;
block|}
DECL|method|testParse_NameEmail5 ()
specifier|public
name|void
name|testParse_NameEmail5
parameter_list|()
block|{
specifier|final
name|Address
name|a
init|=
name|Address
operator|.
name|parse
argument_list|(
literal|"A U Thor<author@example.com>"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"A U Thor"
argument_list|,
name|a
operator|.
name|name
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"author@example.com"
argument_list|,
name|a
operator|.
name|email
argument_list|)
expr_stmt|;
block|}
DECL|method|testParse_Email1 ()
specifier|public
name|void
name|testParse_Email1
parameter_list|()
block|{
specifier|final
name|Address
name|a
init|=
name|Address
operator|.
name|parse
argument_list|(
literal|"author@example.com"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|a
operator|.
name|name
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"author@example.com"
argument_list|,
name|a
operator|.
name|email
argument_list|)
expr_stmt|;
block|}
DECL|method|testParse_Email2 ()
specifier|public
name|void
name|testParse_Email2
parameter_list|()
block|{
specifier|final
name|Address
name|a
init|=
name|Address
operator|.
name|parse
argument_list|(
literal|"a@b"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|a
operator|.
name|name
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a@b"
argument_list|,
name|a
operator|.
name|email
argument_list|)
expr_stmt|;
block|}
DECL|method|testParseInvalid ()
specifier|public
name|void
name|testParseInvalid
parameter_list|()
block|{
name|assertInvalid
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|assertInvalid
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|assertInvalid
argument_list|(
literal|"a<"
argument_list|)
expr_stmt|;
name|assertInvalid
argument_list|(
literal|"<a"
argument_list|)
expr_stmt|;
name|assertInvalid
argument_list|(
literal|"<a>"
argument_list|)
expr_stmt|;
name|assertInvalid
argument_list|(
literal|"a<a>"
argument_list|)
expr_stmt|;
name|assertInvalid
argument_list|(
literal|"a<a>"
argument_list|)
expr_stmt|;
name|assertInvalid
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|assertInvalid
argument_list|(
literal|"a<@"
argument_list|)
expr_stmt|;
name|assertInvalid
argument_list|(
literal|"<a@"
argument_list|)
expr_stmt|;
name|assertInvalid
argument_list|(
literal|"<a@>"
argument_list|)
expr_stmt|;
name|assertInvalid
argument_list|(
literal|"a<a@>"
argument_list|)
expr_stmt|;
name|assertInvalid
argument_list|(
literal|"a<a@>"
argument_list|)
expr_stmt|;
name|assertInvalid
argument_list|(
literal|"a<@a>"
argument_list|)
expr_stmt|;
block|}
DECL|method|assertInvalid (final String in)
specifier|private
specifier|static
name|void
name|assertInvalid
parameter_list|(
specifier|final
name|String
name|in
parameter_list|)
block|{
try|try
block|{
name|Address
operator|.
name|parse
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Incorrectly accepted "
operator|+
name|in
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Invalid email address: "
operator|+
name|in
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testToHeaderString_NameEmail1 ()
specifier|public
name|void
name|testToHeaderString_NameEmail1
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"A<a@a>"
argument_list|,
name|format
argument_list|(
literal|"A"
argument_list|,
literal|"a@a"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToHeaderString_NameEmail2 ()
specifier|public
name|void
name|testToHeaderString_NameEmail2
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"A B<a@a>"
argument_list|,
name|format
argument_list|(
literal|"A B"
argument_list|,
literal|"a@a"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToHeaderString_NameEmail3 ()
specifier|public
name|void
name|testToHeaderString_NameEmail3
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"\"A B. C\"<a@a>"
argument_list|,
name|format
argument_list|(
literal|"A B. C"
argument_list|,
literal|"a@a"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToHeaderString_NameEmail4 ()
specifier|public
name|void
name|testToHeaderString_NameEmail4
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"\"A B, C\"<a@a>"
argument_list|,
name|format
argument_list|(
literal|"A B, C"
argument_list|,
literal|"a@a"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToHeaderString_NameEmail5 ()
specifier|public
name|void
name|testToHeaderString_NameEmail5
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"\"A \\\" C\"<a@a>"
argument_list|,
name|format
argument_list|(
literal|"A \" C"
argument_list|,
literal|"a@a"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToHeaderString_Email1 ()
specifier|public
name|void
name|testToHeaderString_Email1
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"a@a"
argument_list|,
name|format
argument_list|(
literal|null
argument_list|,
literal|"a@a"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToHeaderString_Email2 ()
specifier|public
name|void
name|testToHeaderString_Email2
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"<a,b@a>"
argument_list|,
name|format
argument_list|(
literal|null
argument_list|,
literal|"a,b@a"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|format (final String name, final String email)
specifier|private
specifier|static
name|String
name|format
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|String
name|email
parameter_list|)
block|{
return|return
operator|new
name|Address
argument_list|(
name|name
argument_list|,
name|email
argument_list|)
operator|.
name|toHeaderString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

